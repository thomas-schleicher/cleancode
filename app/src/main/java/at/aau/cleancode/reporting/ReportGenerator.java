package at.aau.cleancode.reporting;

import at.aau.cleancode.models.Page;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public abstract class ReportGenerator {

    private final BufferedWriter outputWriter;

    protected ReportGenerator(OutputStreamWriter outputStreamWriter) {
        this.outputWriter = new BufferedWriter(outputStreamWriter);
    }

    protected final synchronized void writeToOutputWriter(String string) throws IOException {
        this.outputWriter.write(string);
    }

    public abstract void writeFormattedReportToOutputWriter(List<Page> pages) throws IOException;
}
