package at.aau.cleancode.crawler;

import at.aau.cleancode.domain.Link;
import at.aau.cleancode.fetching.JsoupFetcher;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebCrawler {

    private static final Logger LOGGER = Logger.getLogger(WebCrawler.class.getName());
    private static final int DEFAULT_DEPTH = 2;
    private static final int MINIMUM_DEPTH = 0;

    private final Set<String> visitedURLs = ConcurrentHashMap.newKeySet();
    private final ConcurrentLinkedQueue<String> deadLinks;
    private final JsoupFetcher htmlFetcher;
    private final HtmlDocumentProcessor documentProcessor;

    public WebCrawler(JsoupFetcher htmlFetcher, HtmlDocumentProcessor documentProcessor) {
        this.htmlFetcher = htmlFetcher;
        this.documentProcessor = documentProcessor;

        this.deadLinks = new ConcurrentLinkedQueue<>();
    }

    public void crawl(String url) {
        performCrawlAction(url, DEFAULT_DEPTH, null);
        this.documentProcessor.handleDeadLinks(this.deadLinks);
    }

    public void crawl(String url, int depth) {
        performCrawlAction(url, depth, null);
        this.documentProcessor.handleDeadLinks(this.deadLinks);
    }

    public void crawl(String link, Set<String> domains) {
        performCrawlAction(link, DEFAULT_DEPTH, domains);
        this.documentProcessor.handleDeadLinks(this.deadLinks);
    }

    public void crawl(String link, int depth, Set<String> domains) {
        performCrawlAction(link, depth, domains);
        this.documentProcessor.handleDeadLinks(this.deadLinks);
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
            Document document = this.htmlFetcher.fetch(link);
            LOGGER.log(Level.INFO, "Crawling -> {0}", link);
            this.documentProcessor.processDocument(document, newLink -> performCrawlAction(newLink, depth - 1, domains));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Crawling failed for: {0}", link);
            deadLinks.add(link);
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
