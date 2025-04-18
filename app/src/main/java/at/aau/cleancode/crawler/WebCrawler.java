package at.aau.cleancode.crawler;

import at.aau.cleancode.exceptions.AlreadyCrawledException;
import at.aau.cleancode.exceptions.DeadLinkException;
import at.aau.cleancode.exceptions.InvalidDepthException;
import at.aau.cleancode.exceptions.DomainNotAllowedException;
import at.aau.cleancode.fetching.HTMLFetcher;
import at.aau.cleancode.models.Page;
import at.aau.cleancode.reporting.ReportGenerator;

import javax.naming.MalformedLinkException;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebCrawler implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(WebCrawler.class.getName());
    private static final int DEFAULT_DEPTH = 2;

    private final HTMLFetcher<?> htmlFetcher;

    private final PageProcessor pageProcessor;
    private final CrawlController crawlController;
    private final LinkValidator linkValidator;
    private final DeadLinkTracker deadLinkTracker;

    /**
     * WebCrawler should be used in a try-with-resources block to ensure that
     * dead link reporting is properly executed at the end of the crawl.
     */
    public WebCrawler(HTMLFetcher<?> htmlFetcher, ReportGenerator reportGenerator) {
        this.htmlFetcher = htmlFetcher;

        deadLinkTracker = new DeadLinkTracker(reportGenerator);
        pageProcessor = new PageProcessor(reportGenerator);
        crawlController = new CrawlController();
        linkValidator = new LinkValidator();
    }

    public void crawl(String link) {
        crawl(link, DEFAULT_DEPTH, null);
    }

    public void crawl(String link, int depth) {
        crawl(link, depth, null);
    }

    public void crawl(String link, Set<String> domains) {
        crawl(link, DEFAULT_DEPTH, domains);
    }

    public void crawl(String link, int depth, Set<String> domains) {
        crawlPageRecursive(link, depth, domains);
    }

    private void crawlPageRecursive(String pageLink, int depth, Set<String> domains) {
        try {
            Page page = tryCrawlPage(pageLink, depth, domains);
            pageProcessor.process(page, depth, newLink -> crawlPageRecursive(newLink, depth - 1, domains));
        } catch (DeadLinkException e) {
            deadLinkTracker.addDeadLink(pageLink);
        } catch (AlreadyCrawledException | InvalidDepthException | DomainNotAllowedException | MalformedLinkException e) {
            LOGGER.log(Level.INFO, "Skipping link: {0} due to: {1}", new String[]{pageLink, e.getClass().getSimpleName()});
        }
    }

    private Page tryCrawlPage(String pageLink, int depth, Set<String> domains) throws DeadLinkException, InvalidDepthException, AlreadyCrawledException, DomainNotAllowedException, MalformedLinkException {
        if (crawlController.isInvalidDepth(depth)) {
            throw new InvalidDepthException();
        }
        if (linkValidator.isLinkValid(pageLink)) {
            throw new MalformedLinkException();
        }
        if (crawlController.isLinkAlreadyVisited(pageLink)) {
            throw new AlreadyCrawledException();
        }
        if (crawlController.isLinkInvalidForDomains(pageLink, domains)) {
            throw new DomainNotAllowedException();
        }

        crawlController.addToVisitedLinks(pageLink);

        try {
            LOGGER.log(Level.INFO, "Crawling -> {0}", pageLink);
            return this.htmlFetcher.fetchPage(pageLink);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Crawling failed for: {0}", pageLink);
            throw new DeadLinkException();
        }
    }

    @Override
    public void close() {
        deadLinkTracker.reportDeadLinks();
    }
}
