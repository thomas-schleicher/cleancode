package at.aau.cleancode.controller;

import at.aau.cleancode.crawler.WebCrawler;
import at.aau.cleancode.crawler.WebCrawlerFactory;
import at.aau.cleancode.ui.ConsoleUI;
import at.aau.cleancode.ui.UserInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AppControllerTest {

    private static final String TEST_URL = "http://example.com";
    private static final String BAD_URL = "not-a-url";
    private static final String DOMAINS = "foo.com,bar.org";
    private static final String DEPTH_INPUT = "5";
    private static final String FILENAME = "report.md";

    UserInterface ui;
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
    void crawlActionShouldRetryBadUrlThenCrawlWithCorrectParameters() {
        // Arrange: sequence of inputs as per controller's prompts
        given(ui.nextLine()).willReturn(
                "crawl",    // initial action
                BAD_URL,     // 1st URL attempt (invalid)
                TEST_URL,    // 2nd URL attempt (valid)
                DOMAINS,     // domains input
                DEPTH_INPUT, // depth input
                FILENAME     // filename input
        );
        given(crawlerFactory.createMarkdownWebCrawler(FILENAME)).willReturn(crawler);

        // Act
        controller.start();

        // Assert invalid‐URL retry
        verify(ui).printMessage("Invalid URL. Please try again.");

        // crawler invoked with correct args
        Set<String> expectedDomains = Set.of("foo.com", "bar.org");
        verify(crawler).crawl(TEST_URL, 5, expectedDomains);
        verify(crawler).close();
    }
}
