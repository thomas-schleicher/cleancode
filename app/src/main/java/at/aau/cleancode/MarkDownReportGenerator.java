package at.aau.cleancode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MarkDownReportGenerator {

    private final BufferedWriter outputWriter;
    private final HTMLParser htmlParser = new HTMLParser();

    public MarkDownReportGenerator(OutputStreamWriter outputStreamWriter) {
        this.outputWriter = new BufferedWriter(outputStreamWriter);
    }

    public void addPageToReport(Document document) throws IOException {
        Element[] headingElements = htmlParser.extractHeadingsFromDocument(document);
        for (Element headingElement : headingElements) {
            int level = Integer.parseInt(headingElement.nodeName().substring(1));
            outputWriter.write(("#".repeat(level) + " " + headingElement.text() + "\n"));
        }
        outputWriter.flush();
    }
}
