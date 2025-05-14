package at.aau.cleancode.crawler;

import at.aau.cleancode.fetching.HTMLFetcher;
import at.aau.cleancode.fetching.JsoupFetcher;
import at.aau.cleancode.reporting.MarkDownReportGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebCrawlerFactory {
    private static final Logger LOGGER = Logger.getLogger(WebCrawlerFactory.class.getName());

    public WebCrawler createMarkdownWebCrawler(String filename) throws IllegalStateException {
        try (FileWriter fileWriter = new FileWriter(filename + ".md")) {
            HTMLFetcher<?> documentFetcher = new JsoupFetcher();
            MarkDownReportGenerator reportGenerator = new MarkDownReportGenerator(fileWriter);
            return new WebCrawler(documentFetcher, reportGenerator);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to establish file writer!", e);
            throw new IllegalStateException("Cannot create markdown webcrawler");
        }
    }
}
