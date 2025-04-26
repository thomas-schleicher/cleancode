package at.aau.cleancode.utility;

import at.aau.cleancode.ui.ConsoleUI;

public class UserInputValidator {
    private static final ConsoleUI ui = new ConsoleUI();

    private UserInputValidator() {}

    public static boolean checkInputDepth(String depthInput) {
        if (depthInput == null || depthInput.isBlank()) {
            return false;
        }
        //TODO: the output for the user should not be printed from the validator
        try {
            int depth = Integer.parseInt(depthInput);
            if (depth < 0) {
                ui.printMessage("Depth cannot be negative");
                return false;
            }
        } catch (NumberFormatException e) {
            ui.printMessage("Invalid Depth format");
            return false;
        }
        return true;
    }

}
