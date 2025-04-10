package at.aau.cleancode.parsing;

import at.aau.cleancode.parsing.textelements.TextElement;

import java.util.ArrayList;
import java.util.List;

public class Page {

    private List<TextElement> textElements;
    private int crawlDepth;

    public Page() {
        this.textElements = new ArrayList<>();
    }

    public void addTextElement(TextElement textElement) {
        this.textElements.add(textElement);
    }

    public List<TextElement> getTextElements() {
        return textElements;
    }

    public int getCrawlDepth() {
        return crawlDepth;
    }

    public void setCrawlDepth(int crawlDepth) {
        this.crawlDepth = crawlDepth;
    }
}
