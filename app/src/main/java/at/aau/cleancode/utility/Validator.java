package at.aau.cleancode.utility;

public class Validator {

    private Validator() {}

    public static boolean checkInputDepth(String depthInput) {
        if (depthInput.isBlank()) {
            return false;
        }
        //TODO: the output for the user should not be printed from the validator
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

}
