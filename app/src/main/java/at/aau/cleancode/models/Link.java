package at.aau.cleancode.models;

import java.net.URI;
import java.net.URISyntaxException;

public class Link {
    private final String text;
    private final String href;

    public Link(String text, String href) {
        this.text = text;
        this.href = href;
    }

    public String getText() {
        return text;
    }

    public String getHref() {
        return href;
    }

    public static boolean validateLink(String link) {
        try {
            URI _ = new URI(link);
        } catch (NullPointerException | URISyntaxException e) {
            return false;
        }
        String regex = "^(https?):\\/\\/.*";
        return link.matches(regex);
    }

    public static boolean validateLink(Link link) {
        return validateLink(link.getHref());
    }
}