package at.aau.cleancode.crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class DeadLinkTrackerTest {

    private DeadLinkTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new DeadLinkTracker();
    }

    @Test
    void trackerIsEmptyInitially() {
        Assertions.assertTrue(tracker.getDeadLinks().isEmpty());
    }

    @Test
    void addedDeadLinkIsTracked() {
        tracker.addDeadLink("https://www.google.com");
        Set<String> deadLinks = tracker.getDeadLinks();

        Assertions.assertEquals(1, deadLinks.size());
        Assertions.assertTrue(deadLinks.contains("https://www.google.com"));
    }
}