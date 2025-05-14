package at.aau.cleancode.crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(strings = {"https://google.com", "https://test.google.com", "https://www.aau.at"})
    void linkShouldBeInvalidForDomainSet(String link) {
        Set<String> domains = new HashSet<>();
        domains.add("https://www.google.com");
        Assertions.assertTrue(controller.isLinkInvalidForDomains(link, domains));
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://test.www.google.com", "https://other.google.com", "https://google.com"})
    void linkShouldBeValidForDomainSet(String link) {
        Set<String> domains = new HashSet<>();
        domains.add("https://www.google.com");
        domains.add("https://google.com");
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
}