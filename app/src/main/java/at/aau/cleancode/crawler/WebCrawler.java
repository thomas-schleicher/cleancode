package at.aau.cleancode.crawler;

import at.aau.cleancode.fetching.HTMLFetcher;
import at.aau.cleancode.models.Link;
import at.aau.cleancode.models.Page;
import at.aau.cleancode.models.textelements.LinkElement;
import at.aau.cleancode.models.textelements.TextElement;
import at.aau.cleancode.reporting.ReportGenerator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebCrawler {

    private static final Logger LOGGER = Logger.getLogger(WebCrawler.class.getName());
    private static final int DEFAULT_DEPTH = 2;
    private static final int MINIMUM_DEPTH = 0;

    private final Set<String> visitedURLs = ConcurrentHashMap.newKeySet();
    private final ConcurrentLinkedQueue<String> deadLinks;
    private final HTMLFetcher<?> htmlFetcher;
    private final ReportGenerator reportGenerator;

    public WebCrawler(HTMLFetcher<?> htmlFetcher, ReportGenerator reportGenerator) {
        this.htmlFetcher = htmlFetcher;
        this.reportGenerator = reportGenerator;

        this.deadLinks = new ConcurrentLinkedQueue<>();
    }

    public void crawl(String url) {
        performCrawlAction(url, DEFAULT_DEPTH, null);
        handleDeadLinks(this.deadLinks);
    }

    public void crawl(String url, int depth) {
        performCrawlAction(url, depth, null);
        handleDeadLinks(this.deadLinks);
    }

    public void crawl(String link, Set<String> domains) {
        performCrawlAction(link, DEFAULT_DEPTH, domains);
        handleDeadLinks(this.deadLinks);
    }

    public void crawl(String link, int depth, Set<String> domains) {
        performCrawlAction(link, depth, domains);
        handleDeadLinks(this.deadLinks);
    }

    public void performCrawlAction(String link, int depth, Set<String> domains) {
        if (!isLinkValid(link)) {
            LOGGER.log(Level.INFO, "Attempted logging with malformed URL: {0}", link);
            return;
        }

        if (!isValidDepth(depth)) {
            return;
        }

        if (!isWebsiteInDomainSet(link, domains)) {
            LOGGER.log(Level.INFO, "URL is not in the domain set: {0}", link);
            return;
        }

        if (visitedURLs.contains(link)) {
            LOGGER.log(Level.INFO, "URL was already crawled: {0}", link);
            return;
        }

        visitedURLs.add(link);

        try {
            LOGGER.log(Level.INFO, "Crawling -> {0}", link);
            Page page = this.htmlFetcher.fetchPage(link);
            processPage(page, link, newLink -> performCrawlAction(newLink, depth - 1, domains));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Crawling failed for: {0}", link);
            deadLinks.add(link);
        }
    }

    private void processPage(Page page, String link, Consumer<String> linkConsumer) {
        page.setPageUrl(link);
        handleLinksInPage(page, linkConsumer);
        addPageToReport(page);
    }

    private void addPageToReport(Page page) {
        try {
            reportGenerator.addPage(page);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to process page: {0}", page.getPageUrl());
        }
    }

    private void handleLinksInPage(Page page, Consumer<String> linkConsumer) {
        for (TextElement textElement : page.getTextElements()) {
            if (textElement instanceof LinkElement linkElement) {
                linkConsumer.accept(linkElement.getHref());
            }
        }
    }

    private void handleDeadLinks(ConcurrentLinkedQueue<String> deadLinks) {
        try {
            reportGenerator.updateDeadLinks(deadLinks);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to update dead links", e);
        }
    }

    private boolean isLinkValid(String link) {
        return link != null && Link.validateLink(link);
    }

    private boolean isValidDepth(int depth) {
        return depth >= MINIMUM_DEPTH;
    }

    private boolean isWebsiteInDomainSet(String link, Set<String> domains) {
        if (domains == null || domains.isEmpty()) {
            return true;
        }

        try {
            URL url = new URI(link).toURL();
            String host = url.getHost().toLowerCase();

            while (host.contains(".")) {
                if (domains.contains(host)) {
                    return true;
                }
                // Strip the left most subdomain from the host
                host = host.substring(host.indexOf('.') + 1);
            }
        } catch (URISyntaxException | MalformedURLException e) {
            return false;
        }
        return false;
    }
}
