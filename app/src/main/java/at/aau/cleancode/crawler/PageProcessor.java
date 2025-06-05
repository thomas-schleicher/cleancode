package at.aau.cleancode.crawler;

import at.aau.cleancode.models.Page;
import at.aau.cleancode.models.textelements.LinkElement;
import at.aau.cleancode.models.textelements.TextElement;

import java.util.*;
import java.util.function.Predicate;

public class PageProcessor {

    private final List<Page> pages;

    public PageProcessor() {
        this.pages = Collections.synchronizedList(new ArrayList<>());
    }

    public Optional<List<String>> process(Page page, int pageDepth) {
        if (page == null) {
            return Optional.empty();
        }

        page.setPageCrawlDepth(pageDepth);
        pages.add(page);

        return Optional.of(handleLinksInPage(page));
    }

    private List<String> handleLinksInPage(Page page) {
        List<String> links = new ArrayList<>();
        for (TextElement textElement : page.getTextElements()) {
            if (textElement instanceof LinkElement linkElement) {
                links.add(linkElement.getHref());
            }
        }
        return links;
    }

    public void processDeadLinks(Set<String> deadLinks) {
        Predicate<LinkElement> isLinkDead = link -> deadLinks.contains(link.getHref());
        pages.stream()
                .flatMap(page -> page.getTextElements().stream())
                .filter(LinkElement.class::isInstance)
                .map(LinkElement.class::cast)
                .filter(isLinkDead)
                .forEach(LinkElement::setLinkDead);
    }

    public List<Page> getProcessedPages() {
        return pages;
    }
}
