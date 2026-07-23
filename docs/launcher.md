# Launcher

Launcher `io.github.et.conopt4j.launcher.Launcher` mainly serves two main functions:

### 1. Initialize Conopt4j

By calling `Launcher.init(InputStream in)`, you can initialize Conopt4j threads, streams and configs. This is the start of all Conopt4j features.

The parameter `InputStream in` is an InputStream for a property. 

e.g. `Launcher.init(Main.class.getResourceAsStream("/io/github/et/resource.properties"));`

The configuration in properties should be as follows:

```properties
conopt4j.logger.level = Level.DEBUG
conopt4j.logger.info = Color.WHITE
conopt4j.logger.warn = Color.YELLOW
conopt4j.logger.debug = Color.CYAN
conopt4j.logger.error = Color.RED
conopt4j.logger.fatal = Color.PURPLE
conopt4j.logger.severe = Color.RED
conopt4j.logger.fine = Color.BLUE
conopt4j.logger.useTrace = true
conopt4j.logger.output =
conopt4j.logger.maxHistory = 1024
conopt4j.monitor.use = true
conopt4j.status.interval = 500
conopt4j.command.prompt = >
```

* `conopt4j.logger.level` determines the level to be used for a logger, and it must be among `Level.INFO` `Level.DEBUG` and `Level.FINE`

  If this config is not found, the default value will be `Level.INFO`

  > For more about Logger Levels, see [this](./logger.md)

* Configs from`conopt4j.logger.info` to `conopt4j.logger.fine` defines the Color that each logging level uses as its mark. Their value should be among `Color.RED` `Color.GREEN` `Color.YELLOW` `Color.BLUE` `Color.PURPLE` `Color.CYAN` and `Color.WHITE`

  Default value: `Color.WHITE`

  > For more about Logger Colors, see [this](./logger.md)

*  `conopt4j.logger.useTrace ` `conopt4j.logger.output` and `conopt4j.logger.maxHistory`: see [this](./logger.md)

  No default value; Should be provided

* `conopt4j.monitor.use` decides whether to use the function [Monitor](./monitor.md)

  No default value; Should be provided

* `conopt4j.status.interval` determines the refresh interval for [Monitor](./monitor.md)

  No default value; Should be provided

* `conopt4j.command.prompt` stands for the prompt of the Input Line

  No default value; Should be provided

  > For more about Input, see [this](./input.md)

### 2. Register Commands

Commands are not registered in the Command class - they are in Launcher

Call `Launcher.registerCommand(Command cmd)` to register a command

> For more about Commands, see [this](./commands.md)