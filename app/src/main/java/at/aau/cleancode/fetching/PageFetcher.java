package at.aau.cleancode.fetching;

import at.aau.cleancode.parsing.Page;
import at.aau.cleancode.parsing.PageParser;

import java.io.IOException;

public abstract class PageFetcher<PageType> {

    private final PageParser<PageType> parser;

    public PageFetcher(PageParser<PageType> parser) {
        this.parser = parser;
    }

    abstract PageType getRawPage(String url) throws IOException;

    public final Page fetchPage(String url) throws IOException {
        PageType rawPage = getRawPage(url);
        return this.parser.parse(rawPage);
    }
}
