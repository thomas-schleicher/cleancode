package at.aau.cleancode.models;

import at.aau.cleancode.models.textelements.TextElement;

import java.util.ArrayList;
import java.util.List;

public class Page {

    private final List<TextElement> textElements;
    private final String pageUrl;
    private int pageCrawlDepth;

    public Page(String pageUrl) {
        this.pageUrl = pageUrl;
        this.textElements = new ArrayList<>();
    }

    public void addTextElement(TextElement textElement) {
        this.textElements.add(textElement);
    }

    public List<TextElement> getTextElements() {
        return textElements;
    }

    public int getPageCrawlDepth() {
        return pageCrawlDepth;
    }

    public void setPageCrawlDepth(int pageCrawlDepth) {
        this.pageCrawlDepth = pageCrawlDepth;
    }

    public String getPageUrl() {
        return pageUrl;
    }
}
