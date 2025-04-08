package at.aau.cleancode.report;

import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Queue;

import at.aau.cleancode.parsing.textelements.LinkElement;
import at.aau.cleancode.parsing.textelements.TextElement;

public class MarkDownReportGenerator extends ReportGenerator {

    public MarkDownReportGenerator(OutputStreamWriter outputStreamWriter) {
        super(outputStreamWriter);
    }

    @Override
    public void createReport(List<TextElement> textElements) {
        for (TextElement textElement : textElements) {
            if (textElement instanceof LinkElement linkElement) {

                continue;
            }

            //TODO: handle regular text elements
        }


        //TODO: Format the textelements and write them to the outputWriter
        // super.outputWriter.write("");
    }

    @Override
    public void updateDeadLinks(Queue<String> deadLinks) {
        //TODO: Update the links in the document to be marked broken Optimized HashMap + Regex Approach
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
