package at.aau.cleancode;

import org.jsoup.nodes.Document;

public class LinkExtractor {
    public String[] extractLinksFromDocument(Document document) {
        return document.select("a[href]").stream()
                .map(link -> link.absUrl("href"))
                .filter(link -> !link.isEmpty())
                .toArray(String[]::new);
    }
}
