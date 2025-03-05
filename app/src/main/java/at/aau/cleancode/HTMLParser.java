package at.aau.cleancode;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HTMLParser {
    public Element[] extractHeadingsFromDocument(Document document) {
        return document.root().getAllElements().stream()
            .filter(element -> element.nodeName().matches("h[1-6]"))
            .toArray(Element[]::new);
    }

    public String[] extractLinksFromDocument(Document document) {
        return document.select("a[href]").stream()
                .map(link -> link.absUrl("href"))
                .filter(link -> !link.isEmpty())
                .toArray(String[]::new);
    }
}
