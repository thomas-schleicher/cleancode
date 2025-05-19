package at.aau.cleancode.crawler;

import at.aau.cleancode.exceptions.AlreadyCrawledException;
import at.aau.cleancode.exceptions.DeadLinkException;
import at.aau.cleancode.exceptions.DomainNotAllowedException;
import at.aau.cleancode.exceptions.InvalidDepthException;
import at.aau.cleancode.fetching.HTMLFetcher;
import at.aau.cleancode.models.Page;
import at.aau.cleancode.reporting.ReportGenerator;
import at.aau.cleancode.utility.DepthValidator;
import at.aau.cleancode.utility.LinkValidator;

import javax.naming.MalformedLinkException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebCrawler implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(WebCrawler.class.getName());
    private static final int DEFAULT_DEPTH = 2;

    private final HTMLFetcher<?> htmlFetcher;
    private final ReportGenerator reportGenerator;

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
        this.reportGenerator = reportGenerator;

        pageProcessor = new PageProcessor();
        deadLinkTracker = new DeadLinkTracker();
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

    public void crawl(String link, int maxDepth, Set<String> domains) {
        try {
            crawlPageAsync(link, maxDepth, domains).join();
        } catch (CompletionException e) {
            LOGGER.log(Level.SEVERE, "Crawl failed", e);
        }
    }

    private CompletableFuture<Void> crawlPageAsync(String pageLink, int depth, Set<String> domains) {
        var executor = Executors.newVirtualThreadPerTaskExecutor();
        return CompletableFuture.runAsync(() -> {
            Page page = null;
            try {
                page = attemptToCrawlPage(pageLink, depth, domains);
            } catch (DeadLinkException _) {
                deadLinkTracker.addDeadLink(pageLink);
            } catch (AlreadyCrawledException | DomainNotAllowedException | MalformedLinkException e) {
                LOGGER.log(Level.INFO, "Skipping link: {0} due to: {1}", new String[]{pageLink, e.getClass().getSimpleName()});
            } catch (InvalidDepthException _) {
                LOGGER.log(Level.FINEST, "Invalid depth: {0}", depth);
            }

            Optional<List<String>> newlyFoundLinks = pageProcessor.process(page, depth);
            newlyFoundLinks.ifPresent(links -> {
                List<CompletableFuture<Void>> childTasks = links.stream()
                        .map(link -> crawlPageAsync(link, depth - 1, domains))
                        .toList();

                CompletableFuture.allOf(childTasks.toArray(new CompletableFuture[0])).join();
            });
        }, executor);
    }

    private Page attemptToCrawlPage(String pageLink, int depth, Set<String> domains) throws DeadLinkException, InvalidDepthException, AlreadyCrawledException, DomainNotAllowedException, MalformedLinkException {
        if (!DepthValidator.isValidDepth(depth)) {
            throw new InvalidDepthException();
        }
        if (!linkValidator.isLinkValid(pageLink)) {
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
        } catch (IOException _) {
            LOGGER.log(Level.WARNING, "Crawling failed for: {0}", pageLink);
            throw new DeadLinkException();
        }
    }


    public void close() {
        Set<String> deadLinks = deadLinkTracker.getDeadLinks();
        pageProcessor.processDeadLinks(deadLinks);
        List<Page> processedPages = pageProcessor.getProcessedPages();

        try {
            reportGenerator.writeFormattedReportToOutputWriter(processedPages);
            reportGenerator.closeWriter();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error creating report: {0}", e.getMessage());
        }
    }
}
