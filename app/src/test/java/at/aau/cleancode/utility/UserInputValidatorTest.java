package at.aau.cleancode.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserInputValidatorTest {

    @Test
    void testCheckInputDepthCorrectness() {
        assertTrue(UserInputValidator.checkInputDepth("0"), "Zero depth should return true");
        assertTrue(UserInputValidator.checkInputDepth("10"), "Positive depth should return true");
    }

    @Test
    void testCheckInputDepthWithInvalidInputs() {
        assertFalse(UserInputValidator.checkInputDepth("-5"), "Negative depth should return false");
        assertFalse(UserInputValidator.checkInputDepth("abc"), "Invalid depth format should return false");
        assertFalse(UserInputValidator.checkInputDepth(""), "Empty input should return false");
        assertFalse(UserInputValidator.checkInputDepth("   "), "Whitespace input should return false");
    }

    @Test
    void testCheckInputDepthWithNull() {
        assertFalse(UserInputValidator.checkInputDepth(null), "Null input should return false");
    }
}
