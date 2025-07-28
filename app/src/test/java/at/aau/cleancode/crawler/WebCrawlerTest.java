package at.aau.cleancode.crawler;

import at.aau.cleancode.fetching.HTMLFetcher;
import at.aau.cleancode.models.Page;
import at.aau.cleancode.models.textelements.LinkElement;
import at.aau.cleancode.models.textelements.TextElement;
import at.aau.cleancode.reporting.ReportGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

class WebCrawlerTest {

    private HTMLFetcher<?> fetcher;
    private ReportGenerator reportGenerator;
    private WebCrawler crawler;

    @BeforeEach
    void setUp() {
        fetcher = mock(HTMLFetcher.class);
        reportGenerator = mock(ReportGenerator.class);
        crawler = new WebCrawler(fetcher, reportGenerator);
    }

    @Test
    void crawl() throws IOException {
        String parentLink = "https://www.google.com";
        String childLink = "https://www.youtube.com";

        LinkElement linkElementToChildPage = mock(LinkElement.class);
        when(linkElementToChildPage.getHref()).thenReturn(childLink);

        Page parentPage = createMockPage(parentLink, List.of(linkElementToChildPage));
        Page childPage = createMockPage(childLink, List.of());
        when(fetcher.fetchPage(parentLink)).thenReturn(parentPage);
        when(fetcher.fetchPage(childLink)).thenReturn(childPage);

        crawler.crawl(parentLink);

        verify(fetcher, times(1)).fetchPage(parentLink);
        verify(fetcher, times(1)).fetchPage(childLink);
        verify(linkElementToChildPage, atLeastOnce()).getHref();
        verify(reportGenerator, times(0)).writeFormattedReportToOutputWriter(any());
    }

    @Test
    void close() throws IOException {
        String pageLink = "https://www.google.com";
        String otherPageLink = "https://www.youtube.com";

        LinkElement linkElement = mock(LinkElement.class);
        when(linkElement.getHref()).thenReturn(otherPageLink);

        TextElement textElement = mock(TextElement.class);
        when(textElement.getTextContent()).thenReturn("Big Headline");

        Page page = createMockPage(pageLink, List.of(linkElement, textElement));
        when(fetcher.fetchPage(pageLink)).thenReturn(page);

        crawler.crawl(pageLink, 0);
        crawler.close();

        // Depth is 0, so only the initial page should be fetched
        verify(fetcher, times(1)).fetchPage(pageLink);
        verify(fetcher, times(0)).fetchPage(otherPageLink);
        verify(reportGenerator, times(1)).writeFormattedReportToOutputWriter(List.of(page));
    }

    private Page createMockPage(String url, List<TextElement> textElements) {
        Page page = mock(Page.class);
        when(page.getPageUrl()).thenReturn(url);
        when(page.getTextElements()).thenReturn(textElements);
        return page;
    }
}