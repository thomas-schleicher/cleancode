package at.aau.cleancode.controller;

import at.aau.cleancode.ui.ConsoleUI;
import at.aau.cleancode.crawler.WebCrawler;
import at.aau.cleancode.crawler.WebCrawlerFactory;
import at.aau.cleancode.domain.Link;
import at.aau.cleancode.utility.Validator;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AppController {
    private final ConsoleUI ui;
    private final WebCrawlerFactory crawlerFactory;

    public AppController(ConsoleUI ui, WebCrawlerFactory crawlerFactory) {
        this.ui = ui;
        this.crawlerFactory = crawlerFactory;
    }

    public void start() {
        // Show introductory information
        ui.printMessage("This is a simple web crawler that crawls a given URL to a specified depth.");
        ui.printMessage("Available actions:");
        ui.printMessage("1. Crawl a URL (type 'crawl' or 1)");
        ui.printMessage("2. Exit the program (type anything else)");
        ui.printMessage("Please enter the action you wish to perform:");

        String action = ui.nextLine().trim();
        if ("crawl".equalsIgnoreCase(action) || "1".equals(action)) {
            performCrawlAction();
        } else {
            performExitAction();
        }
    }

    private void performExitAction() {
        ui.printMessage("Exiting the program...");
        ui.close();
        System.exit(0);
    }

    private void performCrawlAction() {
        String url = promptForURL();
        Set<String> domains = promptForDomains();
        int depth = promptForDepth();

        ui.printMessage("Crawling URL: " + url
                + " to a depth of " + depth
                + " with domains: " + domains);

        String fileName = promptForFileName();
        WebCrawler crawler = crawlerFactory.createMarkdownWebCrawler(fileName);
        if (crawler == null) {
            ui.printMessage("Failed to create crawler. Please try again.");
            performCrawlAction();
        } else {
            crawler.crawl(url, depth, domains);
        }
    }

    private String promptForURL() {
        ui.printMessage("Please enter the URL you want to crawl:");
        String url = ui.nextLine();
        if (!Link.validateLink(url)) {
            ui.printMessage("Invalid URL. Please try again.");
            return promptForURL();
        }
        return url;
    }

    private Set<String> promptForDomains() {
        ui.printMessage("Enter the domain(s) to be crawled (optional, comma separated):");
        String input = ui.nextLine();
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private int promptForDepth() {
        ui.printMessage("Enter the depth for the crawler (optional):");
        String input = ui.nextLine();
        return Validator.checkInputDepth(input) ? Integer.parseInt(input) : 0;
    }

    private String promptForFileName() {
        ui.printMessage("Please enter the name of the file to be created:");
        String name = ui.nextLine();
        if (name.isBlank()) {
            ui.printMessage("Invalid filename. Please try again.");
            return promptForFileName();
        }
        return name;
    }
}
