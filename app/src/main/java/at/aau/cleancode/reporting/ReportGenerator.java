package at.aau.cleancode.reporting;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Queue;

import at.aau.cleancode.models.Page;

public abstract class ReportGenerator {

    private final BufferedWriter outputWriter;

    protected ReportGenerator(OutputStreamWriter outputStreamWriter) {
        this.outputWriter = new BufferedWriter(outputStreamWriter);
    }

    protected synchronized void writeToOutputWriter(String string) throws IOException {
        this.outputWriter.write(string);
    }

    public abstract void addPage(Page page) throws IOException;

    public abstract void updateDeadLinks(Queue<String> deadLinks) throws IOException;
}
