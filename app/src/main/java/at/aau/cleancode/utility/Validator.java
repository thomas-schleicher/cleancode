package at.aau.cleancode.utility;

import java.net.URI;
import java.net.URISyntaxException;

public class Validator {

    private Validator() {}

    public static boolean checkInputDepth(String depthInput) {
        if (depthInput.isBlank()) {
            return false;
        }
        try {
            int depth = Integer.parseInt(depthInput);
            if (depth < 0) {
                System.out.println("Depth cannot be negative");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Depth format");
            return false;
        }
        return true;
    }

    public static boolean validateURL(String url) {
        try {
            URI _ = new URI(url);
        } catch (NullPointerException | URISyntaxException e) {
            return false;
        }
        String regex = "^(https?):\\/\\/.*";
        return url.matches(regex);
    }

    public static boolean validateAction(String action) {
        if (action == null || action.isEmpty()) {
            System.out.println("Action cannot be null or empty");
            return false;
        }
        try {
            Integer.parseInt(action);
        } catch (NumberFormatException e) {
            System.out.println("The selected action must be a number");
            return false;
        }
        return true;
    }
}
