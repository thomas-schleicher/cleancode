package at.aau.cleancode.fetching;

import at.aau.cleancode.models.Page;
import at.aau.cleancode.parsing.HTMLParser;

import java.io.IOException;

public abstract class HTMLFetcher<T> {

    private final HTMLParser<T> parser;

    protected HTMLFetcher(HTMLParser<T> parser) {
        this.parser = parser;
    }

    abstract T getRawPage(String url) throws IOException;

    public final Page fetchPage(String url) throws IOException {
        T rawPage = getRawPage(url);
        return this.parser.parse(rawPage, url);
    }
}
