package at.aau.cleancode.parsing.textelements;

public class TextElement {

    private final String elementName;
    private final int elementDepth;
    private final String textContent;

    public TextElement(String elementName, int elementDepth, String textContent) {
        this.elementName = elementName;
        this.elementDepth = elementDepth;
        this.textContent = textContent;
    }

    public String getElementName() {
        return elementName;
    }

    public int getElementDepth() {
        return elementDepth;
    }

    public String getTextContent() {
        return textContent;
    }
}
