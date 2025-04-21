package at.aau.cleancode.reporting;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import at.aau.cleancode.models.Page;
import at.aau.cleancode.models.textelements.LinkElement;
import at.aau.cleancode.models.textelements.TextElement;

public class MarkDownReportGenerator extends ReportGenerator {

    public MarkDownReportGenerator(OutputStreamWriter outputStreamWriter) {
        super(outputStreamWriter);
    }

    @Override
    public void writeFormattedReportToOutputWriter(List<Page> pages) throws IOException {
        for (Page page : pages) {
            StringBuilder pageEntry = new StringBuilder();
            appendPageHeaderInformation(page, pageEntry);

            page.getTextElements().forEach(textElement -> {
                if (textElement instanceof LinkElement linkElement) {
                    handleLinkElement(linkElement, pageEntry);
                }
                handleGeneralTextElement(textElement, pageEntry);
            });
            pageEntry.append("\n");
            super.writeToOutputWriter(pageEntry.toString());
        }
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
                .append(">");

        if (linkElement.isDeadLink()) {
            pageEntry.append(" [dead link] to <a>");
        } else {
            pageEntry.append(" link to <a>");
        }

        pageEntry.append(linkElement.getHref())
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
