package at.aau.cleancode.config;

import java.util.logging.*;

public class LoggingConfig {
    private LoggingConfig() {
    }

    public static void configure() {
        LogManager logManager = LogManager.getLogManager();
        logManager.reset();

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(new SimpleFormatter());

        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.ALL);

        rootLogger.addHandler(consoleHandler);
    }
}
