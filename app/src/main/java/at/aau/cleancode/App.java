package at.aau.cleancode;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.aau.cleancode.utility.Validator;

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
        System.out.println("Enter a URL to be crawled: ");
        String url = SCANNER.nextLine();
        if (!Validator.validateURL(url)) {
            System.out.println("Invalid URL");
            return;
        }
        System.out.println("Enter the Depth the crawler should crawl (optional): ");
        String depthInput = SCANNER.nextLine();
        int depth = Validator.checkInputDepth(depthInput) ? Integer.parseInt(depthInput) : 0;
        System.out.println("Crawling URL: " + url + " to a depth of " + depth);

        try(FileWriter fileWriter = new FileWriter("test.md")) {
            MarkDownReportGenerator reportGenerator = new MarkDownReportGenerator(fileWriter);
            WebCrawler crawler = new WebCrawler(new HTMLFetcher(), new HTMLParser(), reportGenerator);
            crawler.crawl(url, depth);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to establish file writer!");
        }
    }
}
