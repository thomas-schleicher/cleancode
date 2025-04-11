package at.aau.cleancode.parsing;

import at.aau.cleancode.models.Page;

public interface HTMLParser<T> {
    Page parse(T input, String pageUrl);
}
