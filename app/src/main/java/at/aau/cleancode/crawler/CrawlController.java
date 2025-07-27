package at.aau.cleancode.crawler;

import at.aau.cleancode.exceptions.AlreadyCrawledException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrawlController {

    private static final Logger LOGGER = Logger.getLogger(CrawlController.class.getName());
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();

    public void markAsVisitedIfNotAlready(String url) throws AlreadyCrawledException {
        if (!visitedUrls.add(url)) {
            LOGGER.log(Level.INFO, "URL was already crawled: {0}", url);
            throw new AlreadyCrawledException();
        }
    }

    public boolean isLinkInvalidForDomains(String url, Set<String> domains) {
        if (domains == null || domains.isEmpty()) {
            return false;
        }

        if (isUrlMatchingADomain(url, domains)) {
            return false;
        }

        LOGGER.log(Level.INFO, "URL is not of the valid domains: {0}", url);
        return true;
    }

    private boolean isUrlMatchingADomain(String url, Set<String> domains) {
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
        return false;
    }
}
