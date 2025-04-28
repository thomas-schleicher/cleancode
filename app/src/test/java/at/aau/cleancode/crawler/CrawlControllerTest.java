package at.aau.cleancode.crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class CrawlControllerTest {

    private CrawlController controller;

    @BeforeEach
    void setUp() {
        controller = new CrawlController();
    }

    @Test
    void linkIsMarkedAsVisitedAfterAdding() {
        String sampleLink = "https://www.google.com";
        Assertions.assertFalse(controller.isLinkAlreadyVisited(sampleLink));
        controller.addToVisitedLinks(sampleLink);
        Assertions.assertTrue(controller.isLinkAlreadyVisited(sampleLink));
    }

    @Test
    void isLinkInvalidForDomains() {
        String sampleLink = "https://www.google.com";
        Set<String> domains = new HashSet<>();

        Assertions.assertAll(
                () -> Assertions.assertFalse(controller.isLinkInvalidForDomains(sampleLink, null)),
                () -> Assertions.assertFalse(controller.isLinkInvalidForDomains(sampleLink, domains)),
                () -> {
                    domains.clear();
                    domains.add("google.de");
                    Assertions.assertTrue(controller.isLinkInvalidForDomains(sampleLink, domains));
                },
                () -> {
                    domains.clear();
                    domains.add("google.com");
                    Assertions.assertFalse(controller.isLinkInvalidForDomains(sampleLink, domains));
                },
                () -> {
                    domains.clear();
                    domains.add("support.google.com");
                    Assertions.assertTrue(controller.isLinkInvalidForDomains(sampleLink, domains));
                },
                () -> {
                    domains.clear();
                    domains.add("www.google.com");
                    Assertions.assertFalse(controller.isLinkInvalidForDomains(sampleLink, domains));
                }
        );
    }

}