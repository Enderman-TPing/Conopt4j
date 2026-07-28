# Demo

Here is a small example to start you off:

```java
package io.github.et.test;

import io.github.et.conopt4j.launcher.Launcher;
import io.github.et.conopt4j.logger.Logger;
import io.github.et.conopt4j.threading.command.Command;
import io.github.et.conopt4j.threading.command.Parameter;
import io.github.et.conopt4j.threading.command.Type;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //initialize with a *.properties file
        Launcher.init(Main.class.getResourceAsStream("/io/github/et/resource.properties"));
        //Progress bar
        new Thread(()-> {
            ProgressBar pb = new ProgressBar();//Initialize progress bar
            pb.setPrefix("Loading");
            pb.setProgress(0);
            pb.setSuffix("0%");
            pb.show();
            int progress=0;
            while(progress<=100){
                pb.setSuffix(progress+"%")
                pb.setProgress(progress++);
                ProgressBar.update();
            }
        }).start();
        
        //try to see if System.out and System.err has been set to logger output
        System.out.println("Hello World!");
        System.err.println("Hello World!");
        System.out.print("Hello ");
        System.err.println("World!");
        
        Logger.warn("warning");
        
        //if you have set useMonitor to true in *.properties, you may fine Monitor already running
        
        //try inputting `filter <some string that you want to search in output history>` or `help` or `help <certain function>` before you register a command
        //these are internal commands
        
        //register command
        Command command =new Command("a");
        command.setDaemon(true)
                .addParameterNode(new Parameter<>("String", Type.STRING))
                    .addExecution(ext-> ext.get("String"))
                .build();
        Launcher.registerCommand(command);
        Command.runCommand("a Hello World!").thenAccept(Logger::info);
        //or try inputting `a Hello World` in console
    }
}

```

