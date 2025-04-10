package at.aau.cleancode.crawler;

import at.aau.cleancode.fetching.JsoupFetcher;
import at.aau.cleancode.report.MarkDownReportGenerator;

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
            HtmlDocumentProcessor processor = new HtmlDocumentProcessor(reportGenerator);
            return new WebCrawler(new JsoupFetcher(), processor);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to establish file writer!", e);
            return null;
        }
    }
}
