package at.aau.cleancode.models.textelements;

public class LinkElement extends TextElement {

    private final String href;

    public LinkElement(String elementName, int elementDepth, String textContent, String href) {
        super(elementName, elementDepth, href);
        this.href = href;
    }

    public String getHref() {
        return href;
    }
}
