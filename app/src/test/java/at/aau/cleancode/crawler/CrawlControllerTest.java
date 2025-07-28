package at.aau.cleancode.crawler;

import at.aau.cleancode.exceptions.AlreadyCrawledException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class CrawlControllerTest {

    private CrawlController controller;

    @BeforeEach
    void setUp() {
        controller = new CrawlController();
    }

    @Test
    void linkIsMarkedAsVisitedAfterAdding() {
        String sampleLink = "https://www.google.com";

        Assertions.assertDoesNotThrow(() -> controller.markAsVisitedIfNotAlready(sampleLink));
        Assertions.assertThrows(AlreadyCrawledException.class, () -> controller.markAsVisitedIfNotAlready(sampleLink));
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://google.com", "https://test.google.com", "https://www.aau.at"})
    void linkShouldBeInvalidForDomainSet(String link) {
        Set<String> domains = new HashSet<>();
        domains.add("www.google.com");
        Assertions.assertTrue(controller.isLinkInvalidForDomains(link, domains));
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://test.www.google.com", "https://other.google.com", "https://google.com"})
    void linkShouldBeValidForDomainSet(String link) {
        Set<String> domains = new HashSet<>();
        domains.add("www.google.com");
        domains.add("google.com");
        Assertions.assertFalse(controller.isLinkInvalidForDomains(link, domains));
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://test.www.google.com", "https://other.google.com", "https://google.com"})
    void linkShouldBeValidWhenDomainSetIsEmpty(String link) {
        Assertions.assertFalse(controller.isLinkInvalidForDomains(link, new HashSet<>()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://test.www.google.com", "https://other.google.com", "https://google.com"})
    void linkShouldBeValidWhenDomainSetIsNull(String link) {
        Assertions.assertFalse(controller.isLinkInvalidForDomains(link, null));
    }

    @Test
    void testConcurrentMarkAsVisited() throws InterruptedException {
        int threadCount = 20;
        int totalUrls = 100;
        int duplicateModulo = 10;

        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger alreadyVisitedCount = new AtomicInteger(0);

        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            for (int t = 0; t < threadCount; t++) {
                executor.submit(() -> {
                    for (int i = 0; i < totalUrls; i++) {
                        // force links to be already visited (as long as totalUrls > duplicateModulo)
                        String url = "https://site" + (i % duplicateModulo) + ".com";
                        try {
                            controller.markAsVisitedIfNotAlready(url);
                        } catch (AlreadyCrawledException _) {
                            alreadyVisitedCount.incrementAndGet();
                        }
                    }
                    latch.countDown();
                });
            }

            latch.await();
            executor.shutdown();
        }

        // here the duplicateModulo represents the count of possible unique urls
        int expectedAmountOfAlreadyVisitedLinks = (totalUrls * threadCount) - duplicateModulo;
        Assertions.assertEquals(expectedAmountOfAlreadyVisitedLinks, alreadyVisitedCount.get());
    }

}