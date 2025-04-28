package at.aau.cleancode.utility;

import at.aau.cleancode.exceptions.InvalidDepthException;

public class UserInputValidator {

    private UserInputValidator() {}

    /// when the input is < 0 or not a number, throw an exception. False is return if the input is null or empty
    public static boolean checkInputDepth(String depthInput) throws InvalidDepthException {
        if (depthInput == null || depthInput.isBlank()) {
            return false;
        }
        try {
            if (Integer.parseInt(depthInput) < 0) throw new InvalidDepthException();
            return Integer.parseInt(depthInput) > 0;
        } catch (NumberFormatException e) {
            throw new InvalidDepthException();
        }
    }

}
