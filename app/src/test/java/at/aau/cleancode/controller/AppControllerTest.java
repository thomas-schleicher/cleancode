package at.aau.cleancode.controller;

import at.aau.cleancode.crawler.WebCrawler;
import at.aau.cleancode.crawler.WebCrawlerFactory;
import at.aau.cleancode.ui.ConsoleUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class AppControllerTest {

    private static final String TEST_URL = "http://example.com";
    private static final String BAD_URL = "not-a-url";
    private static final String DOMAINS = "foo.com,bar.org";
    private static final String DEPTH_INPUT = "5";
    private static final String FILENAME = "report.md";

    ConsoleUI ui;
    WebCrawlerFactory crawlerFactory;
    WebCrawler crawler;
    AppController controller;

    @BeforeEach
    void setUp() {
        ui = mock(ConsoleUI.class);
        crawlerFactory = mock(WebCrawlerFactory.class);
        crawler = mock(WebCrawler.class);
        controller = new AppController(ui, crawlerFactory);
    }

    @Test
    void crawlActionShouldRetryBadUrlThenCrawlWithCorrectParameters() throws Exception {
        // Arrange: sequence of inputs
        given(ui.nextLine()).willReturn(
                "crawl",
                BAD_URL,
                TEST_URL,
                DOMAINS,
                DEPTH_INPUT,
                FILENAME
        );
        given(crawlerFactory.createMarkdownWebCrawler(FILENAME)).willReturn(crawler);

        // Act
        controller.start();

        // Assert invalid‐URL retry
        verify(ui).printMessage("Invalid URL. Please try again.");

        // assert crawl‐summary
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(ui, atLeastOnce()).printMessage(captor.capture());

        String crawlMsg = captor.getAllValues().stream()
                .filter(s -> s.startsWith("Crawling URL:"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Missing crawl message"));

        assertTrue(crawlMsg.contains("Crawling URL: " + TEST_URL));
        assertTrue(crawlMsg.contains("depth of 5"));

        // parse and assert domains set
        String domainsPart = crawlMsg.split("with domains: ")[1];
        Set<String> actual = new HashSet<>(Arrays.asList(domainsPart.replaceAll("[\\[\\]]", "").split(",\\s*")));
        assertEquals(Set.of("foo.com", "bar.org"), actual);

        // crawler invoked with correct args
        verify(crawler).crawl(TEST_URL, 5, actual);
        verify(crawler).close();
    }
}
