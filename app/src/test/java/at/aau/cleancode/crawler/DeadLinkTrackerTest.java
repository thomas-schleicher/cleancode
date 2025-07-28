package at.aau.cleancode.crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Test
    void testConcurrentAdds() throws InterruptedException {
        int threadCount = 20;
        int linksPerThread = 50;
        int expectedLinks = threadCount * linksPerThread;

        CountDownLatch latch = new CountDownLatch(threadCount);

        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            for (int t = 0; t < threadCount; t++) {
                final int threadId = t;
                executor.submit(() -> {
                    for (int i = 0; i < linksPerThread; i++) {
                        tracker.addDeadLink("https://site" + threadId + "-" + i + ".com");
                    }
                    latch.countDown();
                });
            }
            latch.await();
            executor.shutdown();
        }

        Assertions.assertEquals(expectedLinks, tracker.getDeadLinks().size());
    }
}