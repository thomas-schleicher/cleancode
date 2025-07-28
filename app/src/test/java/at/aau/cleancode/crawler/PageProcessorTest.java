package at.aau.cleancode.crawler;

import at.aau.cleancode.models.Page;
import at.aau.cleancode.models.textelements.LinkElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        int arbitraryPageDepth = 1;

        String link = "https://www.google.com";
        LinkElement linkElement = mock(LinkElement.class);
        when(linkElement.getHref()).thenReturn(link);

        Page page = mock(Page.class);
        when(page.getTextElements()).thenReturn(List.of(linkElement));

        Optional<List<String>> result = processor.process(page, arbitraryPageDepth);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(List.of(link), result.get());
        Assertions.assertEquals(1, processor.getProcessedPages().size());
        Assertions.assertEquals(page, processor.getProcessedPages().getFirst());

        verify(linkElement, times(1)).getHref();
        verify(page, times(1)).setPageCrawlDepth(arbitraryPageDepth);
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

        processor.process(page, 1);

        Set<String> deadLinks = Set.of(deadLink);
        processor.processDeadLinks(deadLinks);

        verify(linkElementAlive, times(2)).getHref();
        verify(linkElementDead, times(2)).getHref();
        verify(linkElementAlive, times(0)).setLinkDead();
        verify(linkElementDead, times(1)).setLinkDead();
    }

    @Test
    void getProcessedPages() {
        Page page1 = mock(Page.class);
        Page page2 = mock(Page.class);

        @SuppressWarnings("unchecked")
        Consumer<String> consumer = mock(Consumer.class);

        processor.process(page1, 0);
        processor.process(page2, 1);

        List<Page> processedPages = processor.getProcessedPages();

        Assertions.assertEquals(2, processedPages.size());
        Assertions.assertEquals(page1, processedPages.get(0));
        Assertions.assertEquals(page2, processedPages.get(1));

        verify(consumer, times(0)).accept(anyString());
    }

    @Test
    void testConcurrentProcess() throws InterruptedException {
        int threadCount = 10;
        int pagesPerThread = 20;
        int expectedPageCount = threadCount * pagesPerThread;

        CountDownLatch latch = new CountDownLatch(threadCount);

        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            for (int t = 0; t < threadCount; t++) {
                executor.submit(() -> {
                    for (int i = 0; i < pagesPerThread; i++) {
                        Page page = new Page("https://site" + i + ".com");
                        processor.process(page, 1);
                    }
                    latch.countDown();
                });
            }
            latch.await();
            executor.shutdown();
        }

        Assertions.assertEquals(expectedPageCount, processor.getProcessedPages().size());
    }
}