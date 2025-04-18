package at.aau.cleancode.reporting;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Queue;

import at.aau.cleancode.models.Page;
import at.aau.cleancode.models.textelements.LinkElement;
import at.aau.cleancode.models.textelements.TextElement;

public class MarkDownReportGenerator extends ReportGenerator {

    public MarkDownReportGenerator(OutputStreamWriter outputStreamWriter) {
        super(outputStreamWriter);
    }

    @Override
    public void addPage(Page page) throws IOException {
        StringBuilder pageEntry = new StringBuilder();

        appendPageHeaderInformation(page, pageEntry);
        for (TextElement textElement : page.getTextElements()) {
            if (textElement instanceof LinkElement linkElement) {
                handleLinkElement(linkElement, pageEntry);
                continue;
            }
            handleGeneralTextElement(textElement, pageEntry);
        }

        super.writeToOutputWriter(pageEntry.toString());
    }

    @Override
    public void updateDeadLinks(Queue<String> deadLinks) {
        //TODO: Update the links in the document to be marked broken Optimized HashMap + Regex Approach
    }

    private void appendPageHeaderInformation(Page page, StringBuilder pageEntry) {
        pageEntry.append("input: <a>")
                .append(page.getPageUrl())
                .append("</a>\n")
                .append("<br/>depth: ")
                .append(page.getPageCrawlDepth())
                .append("\n");
    }

    private void handleLinkElement(LinkElement linkElement, StringBuilder pageEntry) {
        pageEntry.append("<br>")
                .append("-".repeat(Math.max(0, linkElement.getElementDepth())))
                .append("> link to <a>")
                .append(linkElement.getHref())
                .append("</a>\n");
    }

    private void handleGeneralTextElement(TextElement textElement, StringBuilder pageEntry) {
        if (textElement.getElementName().matches("h[1-6]")) {
            int headerLevel = Integer.parseInt(textElement.getElementName().substring(1));
            pageEntry.append("#".repeat(headerLevel))
                    .append(" ")
                    .append(textElement.getTextContent())
                    .append("\n");
        }
    }
}
