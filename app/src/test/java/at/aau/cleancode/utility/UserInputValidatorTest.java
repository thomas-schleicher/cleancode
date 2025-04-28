package at.aau.cleancode.utility;

import at.aau.cleancode.exceptions.InvalidDepthException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserInputValidatorTest {

    @Test
    void testCheckInputDepthCorrectness() throws InvalidDepthException {
        assertFalse(UserInputValidator.checkInputDepth("0"), "Zero depth should return false");
        assertTrue(UserInputValidator.checkInputDepth("10"), "Positive depth should return true");
        Assertions.assertDoesNotThrow(() -> UserInputValidator.checkInputDepth("0"), "Zero depth should not throw an exception");
        Assertions.assertDoesNotThrow(() -> UserInputValidator.checkInputDepth("10"), "Zero depth should not throw an exception");

    }

    @Test
    void testCheckInputDepthWithInvalidInputs() {
        Assertions.assertThrows(InvalidDepthException.class,() -> UserInputValidator.checkInputDepth("-5"));
        Assertions.assertThrows(InvalidDepthException.class,() -> UserInputValidator.checkInputDepth("abc"));
    }

    @Test
    void testCheckInputDepthWithNullAndBlank() throws InvalidDepthException {
        assertFalse(UserInputValidator.checkInputDepth(null), "Null input should return false");
        assertFalse(UserInputValidator.checkInputDepth(""), "Empty input should return false");
        assertFalse(UserInputValidator.checkInputDepth("   "), "Whitespace input should return false");
    }
}
