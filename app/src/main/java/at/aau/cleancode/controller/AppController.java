package at.aau.cleancode.controller;

import at.aau.cleancode.crawler.WebCrawler;
import at.aau.cleancode.crawler.WebCrawlerFactory;
import at.aau.cleancode.exceptions.InvalidDepthException;
import at.aau.cleancode.ui.UserInterface;
import at.aau.cleancode.utility.LinkValidator;
import at.aau.cleancode.utility.DepthValidator;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AppController {
    private final UserInterface ui;
    private final WebCrawlerFactory crawlerFactory;
    private final LinkValidator linkValidator;

    public AppController(UserInterface ui, WebCrawlerFactory crawlerFactory) {
        this.ui = ui;
        this.crawlerFactory = crawlerFactory;
        this.linkValidator = new LinkValidator();
    }

    public void start() {
        String initialAction = promptInitialUserAction();
        if ("crawl".equalsIgnoreCase(initialAction) || "1".equals(initialAction)) {
            createAndRunCrawler();
        }
        exitApplication();
    }

    private void createAndRunCrawler() {
        String url = getValidUrlFromUser();
        Set<String> domains = getDomainsFromUser();
        int depth = getValidDepthFromUser();

        try (WebCrawler crawler = createWebCrawler()) {
            if (depth == -1) {
                crawler.crawl(url, domains);
            } else {
                crawler.crawl(url, depth, domains);
            }
        }
    }

    private String promptInitialUserAction() {
        ui.printMessage("This is a simple web crawler that crawls a given URL to a specified depth.");
        ui.printMessage("Available actions:");
        ui.printMessage("1. Crawl a URL (type 'crawl' or 1)");
        ui.printMessage("2. Exit the program (type anything else)");
        ui.printMessage("Please enter the action you wish to perform:");
        return ui.nextLine().trim();
    }

    private void exitApplication() {
        ui.printMessage("Exiting the program...");
        ui.close();
        System.exit(0);
    }

    private WebCrawler createWebCrawler() throws IllegalStateException {
        String fileName = getValidFileNameFromUser();
        return crawlerFactory.createMarkdownWebCrawler(fileName);
    }

    private String getValidUrlFromUser() {
        ui.printMessage("Please enter the URL you want to crawl:");
        String url = ui.nextLine();
        if (!linkValidator.isLinkValid(url)) {
            ui.printMessage("Invalid URL. Please try again.");
            return getValidUrlFromUser();
        }
        return url;
    }

    private Set<String> getDomainsFromUser() {
        ui.printMessage("Enter the domain(s) to be crawled (optional, comma separated):");
        String input = ui.nextLine();
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private int getValidDepthFromUser() {
        ui.printMessage("Enter the depth for the crawler (optional):");
        String input = ui.nextLine();
        return getValidatedInputDepth(input);
    }

    /// depth is optional, defaults to the value set in the webcrawler, if not valid and smaller than 1
    private int getValidatedInputDepth(String input) {
        boolean valid = false;
        try {
            valid = DepthValidator.isValidDepth(input);
        } catch (InvalidDepthException _) {
            ui.printMessage("Invalid depth, using default depth.");
        } catch (NullPointerException _) {
            ui.printMessage("Using default depth.");

        }
        return valid ? Integer.parseInt(input) : -1;
    }

    private String getValidFileNameFromUser() {
        ui.printMessage("Please enter the name of the file to be created:");
        String name = ui.nextLine();
        if (name == null || name.isBlank()) {
            ui.printMessage("Invalid filename. Please try again.");
            return getValidFileNameFromUser();
        }
        return name;
    }
}
