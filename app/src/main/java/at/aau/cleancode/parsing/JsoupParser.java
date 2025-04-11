package at.aau.cleancode.parsing;

import at.aau.cleancode.models.Page;
import at.aau.cleancode.models.textelements.LinkElement;
import at.aau.cleancode.models.textelements.TextElement;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.function.Consumer;

public class JsoupParser implements HTMLParser<Document> {

    @Override
    public Page parse(Document input, String pageUrl) {
        Page page = new Page(pageUrl);
        dfsParsePageForTextElements(input.parentNode(), page::addTextElement, 0);
        return page;
    }

    private void dfsParsePageForTextElements(Node node, Consumer<TextElement> textElementConsumer, int depth) {
        if (!(node instanceof Element element)) {
            return;
        }

        if (elementHasTextContent(element)) {
           handleTextElement(element, depth, textElementConsumer);
        }

        for (Node child : element.childNodes()) {
            dfsParsePageForTextElements(child, textElementConsumer, depth + 1);
        }
    }

    private boolean elementHasTextContent(Element element) {
        return !element.text().isBlank();
    }

    //TODO: Check if there are other text elements that contain other text that should be handled
    private void handleTextElement(Element element, int depthInPageHierarchy, Consumer<TextElement> textElementConsumer) {
        TextElement textElement;
        if (element.nodeName().equals("a")) {
            textElement = new LinkElement(element.nodeName(), depthInPageHierarchy, element.text(), element.absUrl("href"));
        } else {
            textElement = new TextElement(element.nodeName(), depthInPageHierarchy, element.text());
        }
        textElementConsumer.accept(textElement);
    }
}
