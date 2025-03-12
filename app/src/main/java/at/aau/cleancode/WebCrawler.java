package at.aau.cleancode;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.nodes.Document;

import at.aau.cleancode.utility.Link;

public class WebCrawler {

    private static final Logger LOGGER = Logger.getLogger(WebCrawler.class.getName());
    private static final int DEFAULT_DEPTH = 2;
    private static final int MINIMUM_DEPTH = 0;

    private final Set<String> visitedURLs = ConcurrentHashMap.newKeySet();
    private final HTMLFetcher htmlFetcher;
    private final HtmlDocumentProcessor documentProcessor;

    public WebCrawler(HTMLFetcher htmlFetcher, HtmlDocumentProcessor documentProcessor) {
        this.htmlFetcher = htmlFetcher;
        this.documentProcessor = documentProcessor;
    }

    public void crawl(String link) {
        crawl(link, DEFAULT_DEPTH);
    }

    public void crawl(String link, int depth) {
        if (link == null || !Link.validateLink(link)) {
            LOGGER.log(Level.INFO, "Attempted logging with malformed URL: {0}", link);
            return;
        }

        if (depth < MINIMUM_DEPTH) {
            return; 
        }
        
        //TODO: add logic to check if the link has the correct domain

        if (visitedURLs.contains(link)) {
            return;
        }

        visitedURLs.add(link);
        LOGGER.log(Level.INFO, "Crawling -> {0}", link);

        try {
            Document document = this.htmlFetcher.fetch(link);
            //TODO: check status code of htmlFetcher for deadlink
            //TODO: Still not sure how to handle deadlinks
            // this.documentProcessor.reportDeadLink(link);
            this.documentProcessor.processDocument(document, newLink -> crawl(newLink, depth - 1)); 
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Crawling failed for: {0}", link);
        }
    }
}
