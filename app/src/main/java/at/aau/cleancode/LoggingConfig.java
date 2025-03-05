package at.aau.cleancode;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingConfig {
    private LoggingConfig() {}

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
