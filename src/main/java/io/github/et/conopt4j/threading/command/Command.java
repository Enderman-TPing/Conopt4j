package io.github.et.conopt4j.threading.command;

import io.github.et.conopt4j.launcher.Launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


public class Command{
    private String name;
    private boolean daemon=false;
    private ConcurrentHashMap<List<Parameter<?>>, Function<Context,String>> parameterNodeSet = new ConcurrentHashMap<>();
    private List temp1=null;
    private String description;
    public ConcurrentHashMap<List<Parameter<?>>, Function<Context,String>> getParameterNodeSet() {
        return parameterNodeSet;
    }
    public Command setDescription(String description) {
        this.description = description;
        return this;
    }
    public String getDescription() {
        return description;
    }
    public Command(String name){
        this.name=name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public boolean isDaemon(){
        return this.daemon;
    }
    public Command setDaemon(boolean daemon){
        this.daemon=daemon;
        return this;
    }
    public Command addParameterNode(Parameter... parameters) {
        this.temp1 = Arrays.asList(parameters);
        return this;
    }

    public Command addExecution(Function<Context,String> cxt) {
        if (temp1 != null) {
            parameterNodeSet.put(temp1, cxt);
            temp1 = null;
        }
        return this;
    }
    public void build(){}
    public static CompletableFuture<String> runCommand(String line){
        List<String> rawTokens = splitCommand(line);
        String cmdName = rawTokens.get(0);
        List<String> argsTokens = rawTokens.subList(1, rawTokens.size());
        Command cmd = Launcher.getCommands().get(cmdName);
        if (cmd == null) {
            return CompletableFuture.completedFuture("Unknown command: " + cmdName);
        }
        for (Map.Entry<List<Parameter<?>>, Function<Context, String>> entry : cmd.getParameterNodeSet().entrySet()) {
            List<Parameter<?>> params = entry.getKey();
            List<Object> parsedValues = tryParse(params, argsTokens);
            if (parsedValues != null) {
                ConcurrentHashMap<Parameter<?>, Object> map = new ConcurrentHashMap<>();
                for (int i = 0; i < params.size(); i++) {
                    map.put(params.get(i), parsedValues.get(i));
                }
                Context ctx = new Context(map);
                Function<Context,String> executor = entry.getValue();
                CompletableFuture<String> future = new CompletableFuture<>();
                Runnable task = () -> {
                    try {
                        String result = executor.apply(ctx);
                        future.complete(result);
                    } catch (Exception e) {
                        future.completeExceptionally(e);
                    }
                };
                if (cmd.isDaemon()) {
                    Launcher.getThreadPool().execute(task);
                } else {
                    Launcher.getThreadPool0().execute(task);
                }
                return future;
            }

        }
        return CompletableFuture.completedFuture("Invalid arguments for command: " + cmdName);
    }

    public static List<String> splitCommand(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        int i = 0;
        int len = input.length();

        while (i < len) {
            char c = input.charAt(i);

            if (c == '\\' && i + 1 < len) {
                char next = input.charAt(i + 1);
                if (next == '"') {
                    current.append('"');
                    i += 2;
                    continue;
                }
            }

            if (c == '"') {
                int match = findMatchingQuote(input, i + 1);
                if (match != -1) {
                    current.append(input, i + 1, match);
                    i = match + 1;
                    continue;
                } else {
                    current.append(c);
                    i++;
                    continue;
                }
            }

            if (c == ' ' && !inQuotes) {
                boolean hasMoreContent = false;
                for (int j = i + 1; j < len; j++) {
                    if (!Character.isWhitespace(input.charAt(j))) {
                        hasMoreContent = true;
                        break;
                    }
                }
                if (!hasMoreContent) {
                    current.append(c);
                    i++;
                    continue;
                }

                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                i++;
                continue;
            }

            current.append(c);
            i++;
        }

        if (current.length() > 0) {
            tokens.add(current.toString());
        }

        return tokens;
    }

    private static int findMatchingQuote(String s, int start) {
        int len = s.length();
        for (int i = start; i < len; i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < len) {
                i++;
                continue;
            }
            if (c == '"') {
                return i;
            }
        }
        return -1;
    }

    private static List<Object> tryParse(List<Parameter<?>> params, List<String> tokens) {
        if (params.isEmpty()) {
            return tokens.isEmpty() ? new ArrayList<>() : null;
        }
        if (tokens.size() < params.size()) return null;
        if (tokens.size() == params.size()) {
            List<Object> values = new ArrayList<>();
            for (int i = 0; i < params.size(); i++) {
                Object val = params.get(i).getParser().parse(tokens.get(i));
                if (val == null) return null;
                values.add(val);
            }
            return values;
        }

        if (tokens.size() > params.size()) {
            Parameter<?> lastParam = params.get(params.size() - 1);
            if (lastParam.getType() == String.class) {
                int baseCount = params.size() - 1;
                StringBuilder merged = new StringBuilder();
                for (int i = baseCount; i < tokens.size(); i++) {
                    if (i > baseCount) merged.append(' ');
                    merged.append(tokens.get(i));
                }
                List<Object> values = new ArrayList<>();
                for (int i = 0; i < baseCount; i++) {
                    Object val = params.get(i).getParser().parse(tokens.get(i));
                    if (val == null) return null;
                    values.add(val);
                }
                Object lastVal = lastParam.getParser().parse(merged.toString());
                if (lastVal == null) return null;
                values.add(lastVal);
                return values;
            }
        }
        return null;
    }
}
