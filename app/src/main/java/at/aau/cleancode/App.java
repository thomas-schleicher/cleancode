package at.aau.cleancode;

import at.aau.cleancode.config.LoggingConfig;
import at.aau.cleancode.utility.Link;
import at.aau.cleancode.utility.Validator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        LoggingConfig.configure();
        printInformation();
    }

    private static void printInformation() {
        System.out.println("This is a simple web crawler, that crawls a given URL to a given depth.");
        while (true) {
            System.out.println("Following actions are possible:");
            System.out.println("1. Enter a URL to be crawled");
            System.out.println("2. Exit the program");
            System.out.println("Please Enter the action you wish to perform: ");
            String action = SCANNER.nextLine();
            if (Validator.validateAction(action)) {
                performAction(Integer.parseInt(action));
            }
        }
    }

    private static void performAction(int action) {
        switch (action) {
            case 1:
                performCrawlAction();
                break;
            case 2:
                performExitAction();
                break;
            default:
                System.out.println("Invalid Action");
                break;
        }
    }

    private static void performExitAction() {
        System.out.println("Exiting the program");
        SCANNER.close();
        System.exit(0);
    }

    private static void performCrawlAction() {
        System.out.println("Enter a URL to be crawled, separate them by \",\" if you want to crawl multiple URLs: ");
        String url = SCANNER.nextLine();
        var urlsForInputString = createListForURLString(url);

        System.out.println("Enter the Depth the crawler should crawl (optional): ");
        String depthInput = SCANNER.nextLine();
        int depth = Validator.checkInputDepth(depthInput) ? Integer.parseInt(depthInput) : 0;
        int i = 0;
        var threads = new ArrayList<CrawlThread>();
        for (String listElement : urlsForInputString) {
            try (FileWriter fileWriter = new FileWriter(String.format("report_URL%d.md", i++))) {
                System.out.println("Crawling URL: " + listElement + " to a depth of " + depth);
                var crawlThread = new CrawlThread(listElement, depth, fileWriter);
                crawlThread.start();
                threads.add(crawlThread);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to establish file writer!");
            }
        }
        for (CrawlThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Thread was interrupted!", e);
            }
        }
    }

    private static List<String> createListForURLString(String inputURLString) {
        List<String> urls = List.of(inputURLString.split(","));
        List<String> result = new ArrayList<>();
        for (String url : urls) {
            url = url.strip();
            if (Link.validateLink(url)) {
                result.add(url);
            }
        }
        return result;
    }
}
