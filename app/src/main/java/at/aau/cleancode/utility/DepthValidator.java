package at.aau.cleancode.utility;

import at.aau.cleancode.exceptions.InvalidDepthException;

public class DepthValidator {

    private DepthValidator() {
    }

    public static boolean isValidDepth(int depth) {
        return depth >= 0;
    }

    /**
     * @param depthInput A string that contains the depth value.
     * @return Whether the depth parsed from the input is valid or not.
     * @throws NullPointerException If the user-provided string is null or blank.
     * @throws InvalidDepthException If depthInput can't be parsed into a number.
     */
    public static boolean isValidDepth(String depthInput) throws NullPointerException, InvalidDepthException {
        if (depthInput == null || depthInput.isBlank()) {
            throw new NullPointerException("Input is null or empty");
        }
        try {
            return Integer.parseInt(depthInput) >= 0;
        } catch (NumberFormatException _) {
            throw new InvalidDepthException();
        }
    }
}
