package at.aau.cleancode.models.textelements;

public class LinkElement extends TextElement {

    private final String href;
    private boolean isDeadLink;

    /**
     * Links are always assumed to be alive unless they are explicitly changed to be dead.
     *
     * @param elementName
     * @param elementDepth
     * @param textContent
     * @param href
     */
    public LinkElement(String elementName, int elementDepth, String textContent, String href) {
        super(elementName, elementDepth, textContent);
        this.href = href;
        this.isDeadLink = false;
    }

    public String getHref() {
        return href;
    }

    public boolean isDeadLink() {
        return isDeadLink;
    }

    public void setLinkDead() {
        isDeadLink = true;
    }
}
