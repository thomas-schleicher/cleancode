package at.aau.cleancode;

import at.aau.cleancode.report.MarkDownReportGenerator;

import java.io.FileWriter;

public class CrawlThread extends Thread {

    private final String url;
    private final int depth;
    private final WebCrawler crawler;

    public CrawlThread(String url, int depth, FileWriter fileWriter) {
        this.url = url;
        this.depth = depth;
        MarkDownReportGenerator reportGenerator = new MarkDownReportGenerator(fileWriter);
        HtmlDocumentProcessor processor = new HtmlDocumentProcessor(reportGenerator);
        this.crawler = new WebCrawler(new HTMLFetcher(), processor);
    }

    @Override
    public void run() {
        this.crawler.crawl(this.url, this.depth);
    }

}
