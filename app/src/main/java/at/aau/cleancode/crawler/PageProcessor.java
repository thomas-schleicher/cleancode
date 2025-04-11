package at.aau.cleancode.crawler;

import at.aau.cleancode.models.Page;
import at.aau.cleancode.models.textelements.LinkElement;
import at.aau.cleancode.models.textelements.TextElement;
import at.aau.cleancode.reporting.ReportGenerator;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PageProcessor {
    private static final Logger LOGGER = Logger.getLogger(PageProcessor.class.getName());

    private final ReportGenerator reportGenerator;

    public PageProcessor(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    public void process(Page page, int pageDepth, Consumer<String> linkConsumer) {
        page.setPageCrawlDepth(pageDepth);
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
}
