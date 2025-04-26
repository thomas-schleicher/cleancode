package at.aau.cleancode.parsing;

import at.aau.cleancode.models.Page;
import at.aau.cleancode.models.textelements.LinkElement;
import at.aau.cleancode.models.textelements.TextElement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsoupParserTest {
    private static final String BASE_URL = "http://example.com/";
    private JsoupParser parser;

    @BeforeEach
    void setUp() {
        parser = new JsoupParser();
    }

    @Test
    void shouldSetPageUrlOnParsedPage() {
        Document emptyDoc = Jsoup.parse("", BASE_URL);

        Page page = parser.parse(emptyDoc, "http://custom.url/");

        assertEquals("http://custom.url/", page.getPageUrl());
    }

    @Test
    void shouldProduceNoTextElementsForEmptyDocument() {
        Document emptyDoc = Jsoup.parse("", BASE_URL);

        Page page = parser.parse(emptyDoc, BASE_URL);
        List<TextElement> elements = page.getTextElements();

        assertTrue(elements.isEmpty(), "Expected no text elements on an empty document");
    }

    @Test
    void shouldParseLinkElementWithAbsoluteHrefAndCorrectDepth() {
        // Arrange: a single link inside a div
        String html = "<div><a href=\"/path\">LinkText</a></div>";
        Document doc = Jsoup.parse(html, BASE_URL);

        Page page = parser.parse(doc, BASE_URL);
        Optional<LinkElement> optionalLink = page.getTextElements().stream()
                .filter(LinkElement.class::isInstance)
                .map(LinkElement.class::cast)
                .findFirst();

        assertTrue(optionalLink.isPresent(), "Expected a LinkElement");
        LinkElement link = optionalLink.get();
        assertEquals("a", link.getElementName());
        assertEquals("LinkText", link.getTextContent());
        assertEquals("http://example.com/path", link.getHref());
        assertEquals(4, link.getElementDepth());
    }

    @Test
    void shouldParseTextElementForParagraphWithCorrectDepth() {
        // Arrange: a paragraph inside a section
        String html = "<section><p>Hello World</p></section>";
        Document doc = Jsoup.parse(html, BASE_URL);

        Page page = parser.parse(doc, BASE_URL);
        Optional<TextElement> optionalParagraph = page.getTextElements().stream()
                .filter(e -> !(e instanceof LinkElement))
                .filter(e -> "p".equals(e.getElementName()))
                .findFirst();

        assertTrue(optionalParagraph.isPresent(), "Expected a TextElement for <p>");
        TextElement paragraph = optionalParagraph.get();
        assertEquals("Hello World", paragraph.getTextContent());
        assertEquals(4, paragraph.getElementDepth());
    }
}
