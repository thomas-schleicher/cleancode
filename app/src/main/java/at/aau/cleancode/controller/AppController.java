package at.aau.cleancode.controller;

import at.aau.cleancode.exceptions.InvalidDepthException;
import at.aau.cleancode.ui.ConsoleUI;
import at.aau.cleancode.crawler.WebCrawler;
import at.aau.cleancode.crawler.WebCrawlerFactory;
import at.aau.cleancode.utility.LinkValidator;
import at.aau.cleancode.utility.UserInputValidator;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AppController {
    private final ConsoleUI ui;
    private final WebCrawlerFactory crawlerFactory;
    private final LinkValidator linkValidator;

    public AppController(ConsoleUI ui, WebCrawlerFactory crawlerFactory) {
        this.ui = ui;
        this.crawlerFactory = crawlerFactory;
        this.linkValidator = new LinkValidator();
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
                + " to a depth of " + Math.max(depth, 0)
                + " with domains: " + domains);

        String fileName = promptForFileName();
        try (WebCrawler crawler = crawlerFactory.createMarkdownWebCrawler(fileName)) {
            if (crawler == null) {
                ui.printMessage("Failed to create crawler. Please try again.");
                performCrawlAction();
            } else {
                if (depth == -1) crawler.crawl(url, domains);
                else crawler.crawl(url, depth, domains);
            }
        }
    }

    private String promptForURL() {
        ui.printMessage("Please enter the URL you want to crawl:");
        String url = ui.nextLine();
        if (!linkValidator.isLinkValid(url)) {
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
        return getValidatedInputDepth(input);
    }

    /// depth is optional, default to 2, if not valid and smaller than 1
    private int getValidatedInputDepth(String input) {
        boolean valid;
        try {
            valid = UserInputValidator.checkInputDepth(input);
        } catch (InvalidDepthException invalidDepthException) {
            ui.printMessage("Invalid depth. Using default depth of 2.");
            return -1;
        }
        if (!valid) ui.printMessage("Using default depth of 2.");
        return valid ? Integer.parseInt(input) : -1;
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
