package at.aau.cleancode.report;

import java.io.OutputStreamWriter;
import java.util.List;

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
}
