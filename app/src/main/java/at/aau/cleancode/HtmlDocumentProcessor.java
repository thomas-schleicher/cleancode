package at.aau.cleancode;

import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import org.jsoup.nodes.Document;

import at.aau.cleancode.report.ReportGenerator;
import at.aau.cleancode.utility.Link;
import at.aau.cleancode.utility.Pair;

public class HtmlDocumentProcessor {

    private final ReportGenerator reportGenerator;

    protected HtmlDocumentProcessor(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    public void processDocument(Document document, Consumer<String> linkConsumer) {
        List<Pair<String, Object>> textElements = HTMLParser.parseDocumentForTextElements(document);
        for (Pair textElement : textElements) {
            if (textElement.getValue() instanceof Link newLink) {
               linkConsumer.accept(newLink.getHref());
            }
        }
        reportGenerator.createReport(textElements);
    }

    public void handleDeadLinks(Queue<String> deadLinks) {
        reportGenerator.updateDeadLinks(deadLinks);
    }
}
