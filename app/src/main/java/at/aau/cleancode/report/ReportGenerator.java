package at.aau.cleancode.report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Queue;

import at.aau.cleancode.utility.Pair;

public abstract class ReportGenerator {

    private final BufferedWriter outputWriter;

    protected ReportGenerator(OutputStreamWriter outputStreamWriter) {
        this.outputWriter = new BufferedWriter(outputStreamWriter);
    }

    protected synchronized void writeToOutputWriter(String string) throws IOException {
        this.outputWriter.write(string);
    }

    //TODO: Think about creating a new DataType for this instead of List<Pair<String, Object>>
    public abstract void createReport(List<Pair<String, Object>> textElements);

    public abstract void updateDeadLinks(Queue<String> deadLinks);
}
