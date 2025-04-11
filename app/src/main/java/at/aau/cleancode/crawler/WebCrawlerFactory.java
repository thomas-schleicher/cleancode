package at.aau.cleancode.crawler;

import at.aau.cleancode.fetching.JsoupFetcher;
import at.aau.cleancode.reporting.MarkDownReportGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebCrawlerFactory {
    private static final Logger LOGGER = Logger.getLogger(WebCrawlerFactory.class.getName());

    public WebCrawler createMarkdownWebCrawler(String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(fileName + ".md");
            MarkDownReportGenerator reportGenerator = new MarkDownReportGenerator(fileWriter);
            return new WebCrawler(new JsoupFetcher(), reportGenerator);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to establish file writer!", e);
            return null;
        }
    }
}
