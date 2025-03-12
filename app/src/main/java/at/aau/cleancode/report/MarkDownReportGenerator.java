package at.aau.cleancode.report;

import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Queue;

import at.aau.cleancode.utility.Pair;

public class MarkDownReportGenerator extends ReportGenerator {

    public MarkDownReportGenerator(OutputStreamWriter outputStreamWriter) {
        super(outputStreamWriter);
    }

    @Override
    public void createReport(List<Pair<String, Object>> textElements) {
        //TODO: Format the textelements and write them to the outputWriter
        // super.outputWriter.write("");
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateDeadLinks(Queue<String> deadLinks) {
        //TODO: Update the links in the document to be marked broken Optimized HashMap + Regex Approach
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
