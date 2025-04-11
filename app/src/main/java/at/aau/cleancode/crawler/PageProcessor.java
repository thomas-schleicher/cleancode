package at.aau.cleancode.crawler;

import at.aau.cleancode.models.Page;
import at.aau.cleancode.models.textelements.LinkElement;
import at.aau.cleancode.models.textelements.TextElement;
import at.aau.cleancode.reporting.ReportGenerator;

import java.io.IOException;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PageProcessor {
    private static final Logger LOGGER = Logger.getLogger(PageProcessor.class.getName());
    private final ReportGenerator reportGenerator;

    protected PageProcessor(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    public void process(Page page, Consumer<String> linkConsumer) {
        for (TextElement textElement : page.getTextElements()) {
            if (textElement instanceof LinkElement newLink) {
                linkConsumer.accept(newLink.getHref());
            }
        }
        try {
            reportGenerator.addPage(page);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to append page to report", e);
        }
    }

    public void handleDeadLinks(Queue<String> deadLinks) {
        try {
            reportGenerator.updateDeadLinks(deadLinks);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to update dead links", e);
        }
    }
}
