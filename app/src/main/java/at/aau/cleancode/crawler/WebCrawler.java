package at.aau.cleancode.crawler;

import at.aau.cleancode.fetching.HTMLFetcher;
import at.aau.cleancode.models.Page;
import at.aau.cleancode.reporting.ReportGenerator;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebCrawler {

    private static final Logger LOGGER = Logger.getLogger(WebCrawler.class.getName());
    private static final int DEFAULT_DEPTH = 2;

    private final HTMLFetcher<?> htmlFetcher;

    private final PageProcessor pageProcessor;
    private final CrawlController crawlController;
    private final LinkValidator linkValidator;
    private final DeadLinkTracker deadLinkTracker;

    public WebCrawler(HTMLFetcher<?> htmlFetcher, ReportGenerator reportGenerator) {
        this.htmlFetcher = htmlFetcher;

        deadLinkTracker = new DeadLinkTracker(reportGenerator);
        pageProcessor = new PageProcessor(reportGenerator);
        crawlController = new CrawlController();
        linkValidator = new LinkValidator();
    }

    public void crawl(String url) {
        performCrawlAction(url, DEFAULT_DEPTH, null);
        deadLinkTracker.reportDeadLinks();
    }

    public void crawl(String url, int depth) {
        performCrawlAction(url, depth, null);
        deadLinkTracker.reportDeadLinks();
    }

    public void crawl(String link, Set<String> domains) {
        performCrawlAction(link, DEFAULT_DEPTH, domains);
        deadLinkTracker.reportDeadLinks();
    }

    public void crawl(String link, int depth, Set<String> domains) {
        performCrawlAction(link, depth, domains);
        deadLinkTracker.reportDeadLinks();
    }

    public void performCrawlAction(String pageLink, int depth, Set<String> domains) {
        if (
                crawlController.isInvalidDepth(depth) ||
                linkValidator.isLinkValid(pageLink) ||
                crawlController.isLinkAlreadyVisited(pageLink) ||
                crawlController.isInvalidLinkForDomains(pageLink, domains)
        ) {
            return;
        }

        crawlController.addToVisitedLinks(pageLink);

        try {
            LOGGER.log(Level.INFO, "Crawling -> {0}", pageLink);
            Page page = this.htmlFetcher.fetchPage(pageLink);
            pageProcessor.process(page, depth, newLink -> performCrawlAction(newLink, depth - 1, domains));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Crawling failed for: {0}", pageLink);
            deadLinkTracker.addDeadLink(pageLink);
        }
    }
}
