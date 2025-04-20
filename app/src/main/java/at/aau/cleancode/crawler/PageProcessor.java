package at.aau.cleancode.crawler;

import at.aau.cleancode.models.Page;
import at.aau.cleancode.models.textelements.LinkElement;
import at.aau.cleancode.models.textelements.TextElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

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
        pages.forEach(page -> page.getTextElements().forEach(textElement -> {
            if (textElement instanceof LinkElement linkElement && deadLinks.contains(linkElement.getHref())) {
                linkElement.setLinkDead();
            }
        }));
    }

    public List<Page> getProcessedPages() {
        return pages;
    }
}
