package at.aau.cleancode.crawler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrawlController {

    private static final Logger LOGGER = Logger.getLogger(CrawlController.class.getName());
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();

    public boolean isLinkAlreadyVisited(String url) {
        if (visitedUrls.contains(url)) {
            LOGGER.log(Level.INFO, "URL was already crawled: {0}", url);
            return true;
        }
        return false;
    }

    public void addToVisitedLinks(String url) {
        visitedUrls.add(url);
    }

    public boolean isLinkInvalidForDomains(String url, Set<String> domains) {
        if (domains == null || domains.isEmpty()) {
            return true;
        }

        try {
            String host = new URI(url).getHost();
            while (host.contains(".")) {
                if (domains.contains(host)) {
                    return true;
                }
                // Strip the left-most subdomain from the host
                host = host.substring(host.indexOf('.') + 1);
            }
        } catch (URISyntaxException e) {
            LOGGER.log(Level.WARNING, "URI syntax exception", e);
        }

        LOGGER.log(Level.INFO, "URL is not of the valid domains: {0}", url);
        return false;
    }

    public boolean isInvalidDepth(int depth) {
        return depth >= 0;
    }
}
