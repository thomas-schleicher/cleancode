package at.aau.cleancode.crawler;

import at.aau.cleancode.models.Page;
import at.aau.cleancode.models.textelements.LinkElement;
import at.aau.cleancode.models.textelements.TextElement;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PageProcessor {

    private final List<Page> pages;

    public PageProcessor() {
        this.pages = Collections.synchronizedList(new ArrayList<>());
    }

    public void process(Page page, int pageDepth, Consumer<String> linkConsumer) {
        page.setPageCrawlDepth(pageDepth);
        handleLinksInPage(page, linkConsumer);

        pages.add(page);
    }

    private void handleLinksInPage(Page page, Consumer<String> linkConsumer) {
        for (TextElement textElement : page.getTextElements()) {
            if (textElement instanceof LinkElement linkElement) {
                linkConsumer.accept(linkElement.getHref());
            }
        }
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
