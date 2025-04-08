package at.aau.cleancode;

import at.aau.cleancode.config.LoggingConfig;
import at.aau.cleancode.controller.AppController;
import at.aau.cleancode.crawler.WebCrawlerFactory;
import at.aau.cleancode.ui.ConsoleUI;

public class App {
    public static void main(String[] args) {
        // Configure logging settings
        LoggingConfig.configure();

        // Instantiate UI, factory, and controller objects
        ConsoleUI ui = new ConsoleUI();
        WebCrawlerFactory crawlerFactory = new WebCrawlerFactory();
        AppController controller = new AppController(ui, crawlerFactory);

        // Start the application control flow
        controller.start();
    }

}
