package io.github.et.conopt4j.launcher;

import io.github.et.conopt4j.logger.Logger;
import io.github.et.conopt4j.streams.Err;
import io.github.et.conopt4j.streams.Out;
import io.github.et.conopt4j.threading.ThreadingFactory;
import io.github.et.conopt4j.threading.command.Command;
import io.github.et.conopt4j.threading.command.Context;
import io.github.et.conopt4j.threading.command.Parameter;
import io.github.et.conopt4j.threading.command.Type;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import sun.misc.Signal;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

public class Launcher {
    public static Terminal TERMINAL;
    public static LineReader READER;
    private static ExecutorService threadPool = Executors.newCachedThreadPool(new ThreadingFactory());
    private static ExecutorService threadPool0 = Executors.newCachedThreadPool();
    private static Map<String, Command> commands = new HashMap<>();

    public static Map<String,Command> getCommands() {
        return commands;
    }

    public static ExecutorService getThreadPool() {
        return threadPool;
    }

    public static ExecutorService getThreadPool0() {
        return threadPool0;
    }

    public static void registerCommand(Command cmd) {
        commands.put(cmd.getName(), cmd);
    }

    public static void init(InputStream in) throws IOException {
        PropertyLoader.loadProperties(in);
        TERMINAL = TerminalBuilder.builder().system(true).build();
        READER = LineReaderBuilder.builder()
                .terminal(TERMINAL)
                .build();
        Out.initialize();
        Err.initialize();
        Thread main=null;
        Thread thread = new Thread(() -> {
            while (true) {
                String line = READER.readLine(PropertyLoader.getPrompt());
                if (line == null || line.trim().isEmpty()) continue;

                List<String> rawTokens = splitCommand(line);
                if (rawTokens.isEmpty()) continue;

                String cmdName = rawTokens.get(0);
                List<String> argsTokens = rawTokens.subList(1, rawTokens.size());
                Command cmd = commands.get(cmdName);
                if (cmd == null) {
                    System.err.println("Unknown command: " + cmdName);
                    continue;
                }
                boolean executed = false;
                for (Map.Entry<List<Parameter<?>>, Function<Context, String>> entry : cmd.getParameterNodeSet().entrySet()) {
                    List<Parameter<?>> params = entry.getKey();
                    List<Object> parsedValues = tryParse(params, argsTokens);
                    if (parsedValues != null) {
                        ConcurrentHashMap<Parameter<?>, Object> map = new ConcurrentHashMap<>();
                        for (int i = 0; i < params.size(); i++) {
                            map.put(params.get(i), parsedValues.get(i));
                        }
                        Context ctx = new Context(map);
                        Function<Context, String> executor = entry.getValue();
                        if (cmd.isDeamon()) {
                            Launcher.getThreadPool().execute(() -> Logger.warn(executor.apply(ctx)));
                        } else {
                            Launcher.getThreadPool0().execute(() -> Logger.warn(executor.apply(ctx)));
                        }
                        executed = true;
                        break;
                    }
                }

                if (!executed) {
                    System.err.println("Invalid arguments for command: " + cmdName);
                }
            }

        });
        for(Thread a:Thread.getAllStackTraces().keySet()){
            if(a.getName().equals("main")){
                main=a;
                break;
            }
        }
        thread.start();
        Thread finalThread = thread;
        Thread finalMain = main;
        threadPool.execute(() -> {
            while(true) {
                if(!finalThread.isAlive()){
                    finalMain.interrupt();
                }
            }
        });
        internalCommand:{
            /** Console-only command */
            Command filter=new Command("filter");
            filter.setDeamon(true).setDescription("Filter history commands, console only")
                  .addParameterNode(new Parameter<>("filter", Type.STRING))
                          .addExecution(cxt->{
                              String f=cxt.get("filter");
                              for (String i : Logger.getLogHistory()) {
                                  if(!i.contains(f)){
                                      continue;
                                  }
                                  AttributedStringBuilder sb = new AttributedStringBuilder();
                                  int pos = 0;
                                  int idx;
                                  sb.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
                                  sb.append("-> ");
                                  sb.style(AttributedStyle.DEFAULT);
                                  while ((idx = i.indexOf(f, pos)) != -1) {
                                      if (idx > pos) {
                                          sb.append(i.substring(pos, idx), AttributedStyle.DEFAULT);
                                      }
                                      sb.append(
                                              i.substring(idx, idx + f.length()),
                                              AttributedStyle.DEFAULT.background(AttributedStyle.YELLOW)
                                      );
                                      pos = idx + f.length();
                                  }
                                  if (pos < i.length()) {
                                      sb.append(i.substring(pos), AttributedStyle.DEFAULT);
                                  }
                                  READER.printAbove(sb.toAttributedString());
                              }
                              return "";
                          }).build();
            registerCommand(filter);
            Command help=new Command("help");
            help.setDeamon(true).setDescription("Show help")
                    .addParameterNode()
                        .addExecution(context -> {
                            return buildHelp();
                        })
                    .addParameterNode(new Parameter<>("commandName", Type.STRING))
                        .addExecution(context -> {
                            String commandName=context.get("commandName");
                            if(!commands.containsKey(commandName)){
                                return "Command \"" + commandName + "\" does not exist.";
                            }
                            ConcurrentHashMap<List<Parameter<?>>, Function<Context, String>> params = commands.get(commandName).getParameterNodeSet();
                            StringBuilder sb=new StringBuilder();
                            for (List<Parameter<?>> p:params.keySet()){
                                sb.append(context.get("commandName").toString());
                                for (Parameter<?> p1:p){
                                    sb.append(" <").append(p1.getName()).append(">");
                                }
                                sb.append("\n");
                            }
                            return sb.toString();
                        })
                    .build();
            registerCommand(help);
        }

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

    private static String buildHelp(){
        ArrayList<String> help = new ArrayList<>();
        for (Command i :commands.values()){
            help.add(i.getName() + "\t" + i.getDescription());
        }
        sortArrayList(help);
        String a="";
        for (String s:help){
            a+=s+"\n";
        }
        return a;
    }
    private static void sortArrayList(ArrayList<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        list.sort((s1, s2) -> {
            if (s1 == null && s2 == null) return 0;
            if (s1 == null) return -1;
            if (s2 == null) return 1;

            int i = 0, j = 0;
            int len1 = s1.length(), len2 = s2.length();

            while (i < len1 && j < len2) {
                char c1 = s1.charAt(i);
                char c2 = s2.charAt(j);
                if (c1 == ' ') {
                    i++;
                    continue;
                }
                if (c2 == ' ') {
                    j++;
                    continue;
                }
                int rank1 = isEnglishLetter(c1) ? 0 : 1;
                int rank2 = isEnglishLetter(c2) ? 0 : 1;
                if (rank1 != rank2) {
                    return rank1 - rank2;
                }

                if (rank1 == 0) {
                    int diff = Character.toLowerCase(c1) - Character.toLowerCase(c2);
                    if (diff != 0) return diff;
                    diff = c1 - c2;
                    if (diff != 0) return diff;
                } else {
                    int diff = c1 - c2;
                    if (diff != 0) return diff;
                }

                i++;
                j++;
            }
            while (i < len1 && s1.charAt(i) == ' ') i++;
            while (j < len2 && s2.charAt(j) == ' ') j++;
            return (len1 - i) - (len2 - j);
        });
    }
    private static boolean isEnglishLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }
}