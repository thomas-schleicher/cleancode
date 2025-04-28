package at.aau.cleancode.utility;

import at.aau.cleancode.exceptions.InvalidDepthException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepthValidatorTest {

    @Test
    void testCheckInputDepthCorrectness() throws InvalidDepthException {
        assertTrue(DepthValidator.isValidDepth("0"));
        assertTrue(DepthValidator.isValidDepth("10"));
        Assertions.assertDoesNotThrow(() -> DepthValidator.isValidDepth("0"));
        Assertions.assertDoesNotThrow(() -> DepthValidator.isValidDepth("10"));

    }

    @Test
    void testCheckInputDepthWithInvalidInputs() throws InvalidDepthException {
        assertFalse(DepthValidator.isValidDepth("-5"));
        Assertions.assertThrows(InvalidDepthException.class,() -> DepthValidator.isValidDepth("abc"));
    }

    @Test
    void testCheckInputDepthWithNullAndBlank() {
        Assertions.assertThrows(NullPointerException.class, () -> DepthValidator.isValidDepth(null));
        Assertions.assertThrows(NullPointerException.class, () -> DepthValidator.isValidDepth(""));
        Assertions.assertThrows(NullPointerException.class, () -> DepthValidator.isValidDepth("   "));
    }
}
