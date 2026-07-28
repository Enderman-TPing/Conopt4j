# Commands

### Logic

See `Register a command` part below.

By building such a command, you assign every command certain parameter sets, and each parameter set is assigned certain operation.

Different executions are assigned to different thread pools depending on what you set with `setDaemon()`. `setDaemon(true)` will be assigned to a `CachedThreadPool` with a maximum thread value of 200 and contains of only daemon threads. `setDaemon(false)` will be assigned to a `CachedThreadPool` with a maximum thread value of 200 and keep alive time of 1 second.

All command parameters are defined with its Type and parameters in command strings are transformed into its matching Type.

To shutdown the thread pools, you can call `Launcher.shutdownAllThreadPoolsForcibly()` to forcibly shutdown the two thread pools. You may also call `Launcher.shutdownAllThreadPools(long timeout, TimeUnit unit)` to gracefully shutdown the thread pools with the given timeout value

### Input Bar

After calling `Launcher.init()` , a line that is specifically for input will be created. The prompt specified in the properties file mentioned in the [Launcher part](./docs/launcher.md) will be displayed at the beginning of the line. 

Different from `System.Console.readLine(String prompt)`, you will not be disturbed when inputting with other log output.

### Register a command

I will explain with an example:

``` java
Command printAAA = new Command("printAAA");
//Define the command and its name (the first word of a command)
printAAA.setDescription("Print the String that comes after AAA")
    .setDeamon(true)//if you hope it will be run on a deamon thread
    .addParameterNode(new Parameter<>("String",Type.STRING))
    //public Console addParameterNode(Parameter...parameters)
    //You may add as many parameters as you like
    //Each parameter contains to features: name and type
    //name is shown in help part (which we will talk about later) and is used in the .addExecution() part
    //type defines the type of the Parameter. Only Type.INTEGER, Type.STRING, Type.BOOLEAN, Type.LONG and Type.DOUBLE are supported
        .addExecution({context->{
            return context.get("String")//Here, "String" is the parameter name.
                //in this lambda expression, return value must be String
                //Because in parameter declaration, the "String" is a String, it is automatically converted to a String
                //Any context.get() can invoke such conversion automatically
        }})
    //Here, you can still add more ".addParamaterNode()" and ".addExecution()" to adapt to more input occasions
    // Take notice that all ".addParameterNode()"'s must be followed by an ".addExecution()", even if there is no parameter.
    //If there is no parameter, then directly call .addParameterNode() without any parameters
    .build();//build() is only a mark that serves no use. can be omitted.
Launcher.registerCommand(printAAA)
```

If your given execution may raise an exception, please deal with it by yourself. There is no default exception handler so that you can define your return value yourself.

#### How to Use the Command

If the command is inputted in the terminal, the return value of the addExecution lambda will be directly printed in the terminal as a reply for the command.

If you want to run the command in some other places,  you can call `Command.runCommand(String command)`, which returns a `CompletableFuture<String>`

Example (with the registered command shown in the `Register a command` part):

```java
Command.runCommand("printAAA aaa").thenAccept(System.out::println);
```

The result should be:

```text
aaa
```



### Internal Commands

There are some internal commands:

##### filter

`filter` is used for searching for certain String that appeared in the logging history

<bold>GRAMMAR: filter \<certain String that you want to search\></bold>

This is a console-only command, which will not return needed information when called by `Command.runCommand()`

All lines of log that contains the specified String will be printed with the beginning of a green arrow. What's more, the String will be marked yellow background in each line.

##### help

`help` is used to show help 

GRAMMAR:

<bold>help</bold>

<bold>help \<command name></bold>

With no parameters, it will give off all command name+descriptions

With parameter, it will give off all command usage under this name

### Tip

For occasions when a space appears in a String-typed parameter, you should quote the parameter with `""`. If quotation marks should be included in a String-types parameter, it should go with a slash: `\"`

However if spaces appear in the last parameter of the command, quotation marks can be omitted.