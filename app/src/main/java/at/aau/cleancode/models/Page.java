package at.aau.cleancode.models;

import at.aau.cleancode.models.textelements.TextElement;

import java.util.ArrayList;
import java.util.List;

public class Page {

    private final List<TextElement> textElements;
    private int pageCrawlDepth;
    private final String pageUrl;

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

    public void setPageCrawlDepth(int pageCrawlDepth) {
        this.pageCrawlDepth = pageCrawlDepth;
    }

    public int getPageCrawlDepth() {
        return pageCrawlDepth;
    }

    public String getPageUrl() {
        return pageUrl;
    }
}
