package at.aau.cleancode;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.nodes.Document;

import at.aau.cleancode.utility.Validator;

public class WebCrawler {

    private static final Logger LOGGER = Logger.getLogger(WebCrawler.class.getName());
    private static final int DEFAULT_DEPTH = 2;
    private static final int MINIMUM_DEPTH = 0;

    private final Set<String> visitedURLs = ConcurrentHashMap.newKeySet();
    private final HTMLFetcher htmlFetcher;
    private final HTMLParser htmlParser;
    private final MarkDownReportGenerator reportGenerator;

    public WebCrawler(HTMLFetcher htmlFetcher, HTMLParser htmlParser, MarkDownReportGenerator reportGenerator) {
        this.htmlFetcher = htmlFetcher;
        this.htmlParser = htmlParser;  
        this.reportGenerator = reportGenerator;
    }

    public void crawl(String url) {
        crawl(url, DEFAULT_DEPTH);
    }

    public void crawl(String url, int depth) {
        if (url == null || !Validator.validateURL(url)) {
            LOGGER.log(Level.INFO, "Attempted logging with malformed URL: {0}", url);
            return;
        }

        if (depth < MINIMUM_DEPTH) {
            return; 
        }

        if (visitedURLs.contains(url)) {
            return;
        }

        visitedURLs.add(url);
        LOGGER.log(Level.INFO, "Crawling -> {0}", url);

        try {
            Document document = this.htmlFetcher.fetch(url);
            this.reportGenerator.addPageToReport(document);

            for (String nestedUrl : this.htmlParser.extractLinksFromDocument(document)) {
                crawl(nestedUrl, depth - 1);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Crawling failed for: {0}", url);
        }
    }
}
