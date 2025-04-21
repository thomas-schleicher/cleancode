package at.aau.cleancode.crawler;

import at.aau.cleancode.models.Page;
import at.aau.cleancode.models.textelements.LinkElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

class PageProcessorTest {

    private PageProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new PageProcessor();
    }

    @Test
    void process() {
        int arbitraryDepth = 1;

        String link = "https://www.google.com";
        LinkElement linkElement = mock(LinkElement.class);
        when(linkElement.getHref()).thenReturn(link);

        Page page = mock(Page.class);
        when(page.getTextElements()).thenReturn(List.of(linkElement));

        @SuppressWarnings("unchecked")
        Consumer<String> consumer = mock(Consumer.class);

        processor.process(page, arbitraryDepth, consumer);

        Assertions.assertEquals(1, processor.getProcessedPages().size());
        Assertions.assertEquals(page, processor.getProcessedPages().getFirst());

        verify(linkElement, times(1)).getHref();
        verify(page, times(1)).setPageCrawlDepth(arbitraryDepth);
        verify(consumer).accept(link);
    }

    @Test
    void processDeadLinks() {
        String aliveLink = "https://www.google.com";
        String deadLink = "https://www.support.google.com";

        LinkElement linkElementAlive = mock(LinkElement.class);
        LinkElement linkElementDead = mock(LinkElement.class);
        when(linkElementAlive.getHref()).thenReturn(aliveLink);
        when(linkElementDead.getHref()).thenReturn(deadLink);

        Page page = mock(Page.class);
        when(page.getTextElements()).thenReturn(List.of(linkElementAlive, linkElementDead));

        @SuppressWarnings("unchecked")
        Consumer<String> consumer = mock(Consumer.class);

        processor.process(page, 1, consumer);

        Set<String> deadLinks = Set.of(deadLink);
        processor.processDeadLinks(deadLinks);

        verify(linkElementAlive, times(2)).getHref();
        verify(linkElementDead, times(2)).getHref();
        verify(linkElementAlive, times(0)).setLinkDead();
        verify(linkElementDead, times(1)).setLinkDead();
        verify(consumer).accept(aliveLink);
        verify(consumer).accept(deadLink);
    }

    @Test
    void getProcessedPages() {
        Page page1 = mock(Page.class);
        Page page2 = mock(Page.class);

        @SuppressWarnings("unchecked")
        Consumer<String> consumer = mock(Consumer.class);

        processor.process(page1, 0, consumer);
        processor.process(page2, 1, consumer);

        List<Page> processedPages = processor.getProcessedPages();

        Assertions.assertEquals(2, processedPages.size());
        Assertions.assertEquals(page1, processedPages.get(0));
        Assertions.assertEquals(page2, processedPages.get(1));

        verify(consumer, times(0)).accept(anyString());
    }
}