package at.aau.cleancode.crawler;

import at.aau.cleancode.reporting.ReportGenerator;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeadLinkTracker {

    private static final Logger LOGGER = Logger.getLogger(DeadLinkTracker.class.getName());
    private final Queue<String> deadLinks = new ConcurrentLinkedQueue<>();

    private final ReportGenerator reportGenerator;

    public DeadLinkTracker(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    public void addDeadLink(String url) {
        deadLinks.add(url);
    }

    public void reportDeadLinks() {
        try {
            reportGenerator.updateDeadLinks(deadLinks);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Dead links could not be reported", e);
        }
    }
}
