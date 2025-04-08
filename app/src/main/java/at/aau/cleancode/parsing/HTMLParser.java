package at.aau.cleancode.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.aau.cleancode.parsing.textelements.LinkElement;
import at.aau.cleancode.parsing.textelements.TextElement;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class HTMLParser {

    private static final Logger LOGGER = Logger.getLogger(HTMLParser.class.getName());

    private HTMLParser() {
    }

    public static List<TextElement> parseDocumentForTextElements(Document document) {
        List<TextElement> extractedData = new ArrayList<>();
        dfsParseTextElements(document, extractedData, 0);
        return extractedData;
    }

    private static void dfsParseTextElements(Node node, List<TextElement> extractedData, int depth) {
        if (!(node instanceof Element element)) {
            return;
        }

        if (elementHasTextContent(element)) {
            switch (element.nodeName()) {
                case "a" -> {
                    LinkElement foundLink = new LinkElement(element.nodeName(), depth, element.text(), element.absUrl("href"));
                    extractedData.add(foundLink);
                }
                case "tt", "i", "b", "big", "small", "em", "strong", "dfn", "code", "samp", "kbd", "var", "cite",
                     "abbr", "acronym", "sub", "sup", "span", "bdo", "address", "div", "object", "p", "h1", "h2", "h3",
                     "h4", "h5", "h6", "pre", "q", "ins", "del", "dt", "dd", "li", "label", "option", "textarea",
                     "fieldset", "legend", "button", "caption", "td", "th", "title", "script", "style", "blockquote" ->
                        extractedData.add(new TextElement(element.nodeName(), depth, element.text()));
                default ->
                    LOGGER.log(Level.INFO, "Found unknown element with text: {0}", element.nodeName());
            }
        }

        for (Node child : element.childNodes()) {
            dfsParseTextElements(child, extractedData, depth + 1);
        }
    }

    private static boolean elementHasTextContent(Element element) {
        return !element.text().isBlank();
    }
}