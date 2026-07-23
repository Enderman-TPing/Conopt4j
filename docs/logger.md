# Logger

> The class Logger and some of its related classes have detailed javadoc. You may turn to them whenever they're needed

The logger is slightly different from the mainstream logging mode:

### Levels

There are `info` `warn` `fatal` `fine` `severe` `error` and `debug`

You can simply call `Logger.info(String log)` to write an INFO log to console

> Before reading the contents below, please make sure that you have read [Launcher](./docs/launcher.md)

In the properties file mentioned in Launcher, there is the `level` part

If you set `conopt4j.logger.level = Level.DEBUG`, all logging output will be shown

If you set `conopt4j.logger.level = Level.SEVERE`, `debug` logging output will be hidden

If you set `conopt4j.logger.level = Level.INFO`, only `info` `error` `warn` `fatal` will be shown

#### `System.out` and `System.error` Redirect

Output for `System.out` is redirected to `Logger.info()`, and `System.err` is redirected to `Logger.error()`

As for `System.out/err.print()`, the content will not be printed until an endline is found. 

e.g.

```java
System.out.print("Hello ");
System.err.println("World!");//or Logger.error("World!");
```

is equivalent to 

```java
System.out.println("Hello World!");
//or
Logger.info("Hello World!");
```



and



```java
System.err.print("Hello ");
System.out.println("World!");//or Logger.error("World!");
```

is equivalent to

```java
System.err.println("Hello World!");
//or
Logger.error("Hello World!");
```

If you want to output without the Logger format just as `System.out/err` originally does (not 100% the same), you may call functions methods like `Out.OUT.println()` `Err.ERR.println()`.

### File Output

You may set this in the properties file mentioned in [Launcher](./docs/launcher.md).

```properties
conopt4j.logger.output =
```

The value refers to the path of the output file. When the value is `null` (that is, empty) , logs will not be written into a file.

You can also manually set path by calling `Logger.setFileOutPut(String fileOutPut)`

### Logger History

Mainly used by a command (see [Command](./docs/command.md))

Determined by the `conopt4j.logger.maxHistory` config, which tells the max history logging output count that Conopt4j will record.

### Style

The style of the Logger output is determined by 

```properties
conopt4j.logger.info = Color.WHITE
conopt4j.logger.warn = Color.YELLOW
conopt4j.logger.debug = Color.CYAN
conopt4j.logger.error = Color.RED
conopt4j.logger.fatal = Color.PURPLE
conopt4j.logger.severe = Color.RED
conopt4j.logger.fine = Color.BLUE
conopt4j.logger.useTrace = true
```

all these configs.

The example above will give off results like this:

[<font color=#00FFFF>DEBUG</font>]<font color=yellow>--io.github.et.Main--</font>Hello!

[<font color=red>ERROR</font>]<font color=yellow>--io.github.et.Main--</font>Hello!

[<font color=yellow>WARN</font>]<font color=yellow>--io.github.et.Main--</font>Hello!

If `useTrace` is false, then trace (like the `--io.github.et.Main--` will not show)

> TODO: Considering adding time and date

