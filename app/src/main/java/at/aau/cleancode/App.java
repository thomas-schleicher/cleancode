package at.aau.cleancode;

import at.aau.cleancode.config.LoggingConfig;
import at.aau.cleancode.controller.AppController;
import at.aau.cleancode.crawler.WebCrawlerFactory;
import at.aau.cleancode.ui.ConsoleUI;
import at.aau.cleancode.ui.UserInterface;

public class App {
    public static void main(String[] args) {
        LoggingConfig.configure();

        UserInterface ui = new ConsoleUI();
        WebCrawlerFactory crawlerFactory = new WebCrawlerFactory();
        new AppController(ui, crawlerFactory).start();
    }
}
