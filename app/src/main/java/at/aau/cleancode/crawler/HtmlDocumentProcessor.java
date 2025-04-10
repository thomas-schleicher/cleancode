package at.aau.cleancode.crawler;

import at.aau.cleancode.parsing.HTMLParser;
import at.aau.cleancode.parsing.textelements.LinkElement;
import at.aau.cleancode.parsing.textelements.TextElement;
import at.aau.cleancode.report.ReportGenerator;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HtmlDocumentProcessor {
    private static final Logger LOGGER = Logger.getLogger(HtmlDocumentProcessor.class.getName());
    private final ReportGenerator reportGenerator;

    protected HtmlDocumentProcessor(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    public void processDocument(Document document, Consumer<String> linkConsumer) {
        List<TextElement> textElements = HTMLParser.parseDocumentForTextElements(document);
        for (TextElement textElement : textElements) {
            if (textElement instanceof LinkElement newLink) {
                linkConsumer.accept(newLink.getHref());
            }
        }
        try {
            reportGenerator.appendTextElementsToReport(textElements);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to append page to report", e);
        }
    }

    public void handleDeadLinks(Queue<String> deadLinks) {
        reportGenerator.updateDeadLinks(deadLinks);
    }
}
