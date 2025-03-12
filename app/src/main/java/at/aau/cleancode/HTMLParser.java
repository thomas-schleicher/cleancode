package at.aau.cleancode;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import at.aau.cleancode.utility.Link;
import at.aau.cleancode.utility.Pair;

public class HTMLParser {

    private HTMLParser() {
    }

    public static List<Pair<String, Object>> parseDocumentForTextElements(Document document) {
        List<Pair<String, Object>> extractedData = new ArrayList<>();
        dfsParseTextElements(document, extractedData);
        return extractedData;
    }

    private static void dfsParseTextElements(Node node, List<Pair<String, Object>> extractedData) {
        if (!(node instanceof Element)) {
            return;
        }

        Element element = (Element) node;
        switch (element.nodeName()) {
            case "a" -> {
                Link foundLink = new Link(element.text(), element.absUrl("href"));
                extractedData.add(new Pair<>(element.nodeName(), foundLink));
            }
            case "tt", "i", "b", "big", "small", "em", "strong", "dfn", "code", "samp", "kbd", "var", "cite", "abbr", "acronym", "sub", "sup", "span", "bdo", "address", "div", "object", "p", "h1", "h2", "h3", "h4", "h5", "h6", "pre", "q", "ins", "del", "dt", "dd", "li", "label", "option", "textarea", "fieldset", "legend", "button", "caption", "td", "th", "title", "script", "style", "blockquote" ->
                extractedData.add(new Pair<>(element.nodeName(), element.text()));
        }

        for (Node child : element.childNodes()) {
            dfsParseTextElements(child, extractedData);
        }
    }

}
