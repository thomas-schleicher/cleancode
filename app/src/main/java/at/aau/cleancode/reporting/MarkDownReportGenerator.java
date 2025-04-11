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
        for (TextElement textElement : page.getTextElements()) {
            StringBuilder markdownStringBuilder = new StringBuilder();
            if (textElement instanceof LinkElement linkElement) {
                markdownStringBuilder.append("<br>");
                markdownStringBuilder.append("-".repeat(Math.max(0, linkElement.getElementDepth())));
                markdownStringBuilder.append("> link to <a>");
                markdownStringBuilder.append(linkElement.getHref());
                markdownStringBuilder.append("</a>");
            } else {
                //TODO: format different text elements
            }

            super.writeToOutputWriter(markdownStringBuilder.toString());
        }
    }

    @Override
    public void updateDeadLinks(Queue<String> deadLinks) {
        //TODO: Update the links in the document to be marked broken Optimized HashMap + Regex Approach
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
