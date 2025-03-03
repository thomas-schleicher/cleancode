package at.aau.cleancode;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import at.aau.cleancode.utility.Validator;

public class SiteCrawler {

    private final Set<String> visitedURLs = ConcurrentHashMap.newKeySet();

    public void crawl(String url) {
        crawl(url, 2);
    }

    public void crawl(String url, int depth) {

        if (!Validator.checkURL(url)) {
            return;
        }

        if (depth < 0) {
            return; //TODO: Figure out how to handle magic number
        }

        if (visitedURLs.contains(url)) {
            return;
        }

        visitedURLs.add(url);

        Document document = getHTMLDocument(url);
        if (document == null) {
            //TODO: Error handling
            return;
        }

        String[] nestedLinks = getNestedLinks(document);
        for (String nestedUrl : nestedLinks) {
            System.out.println("Crawling New Link -> " + nestedUrl);
            crawl(nestedUrl, depth - 1);
        }
    }

    private Document getHTMLDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }
    }

    private String[] getNestedLinks(Document document) {
        return document.select("a[href]").stream()
                .map(link -> link.absUrl("href"))
                .filter(link -> !link.isEmpty())
                .toArray(String[]::new);
    }
}
