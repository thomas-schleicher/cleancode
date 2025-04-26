package at.aau.cleancode.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConsoleUITest {
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    private ByteArrayOutputStream outContent;
    private ConsoleUI ui;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ui = new ConsoleUI();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
        ui.close();
    }

    @Test
    void testPrintMessage() {
        ui.printMessage("Hello, World!");
        assertEquals("Hello, World!" + System.lineSeparator(),
                outContent.toString());
    }

    @Test
    void testCloseDisablesScanner() {
        System.setIn(new ByteArrayInputStream(new byte[0]));
        ui.close();

        assertThrows(IllegalStateException.class, () -> ui.nextLine(),
                "After close(), scanner.nextLine() must throw IllegalStateException");
    }

    @Test
    void testTryWithResourcesClosesAutomatically() {
        String simulated = "foo";
        System.setIn(new ByteArrayInputStream((simulated + System.lineSeparator()).getBytes()));

        try (ConsoleUI autoUi = new ConsoleUI()) {
            assertEquals("foo", autoUi.nextLine());
        }

        assertThrows(NoSuchElementException.class, () -> ui.nextLine());
    }

}
