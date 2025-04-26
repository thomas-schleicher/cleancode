package at.aau.cleancode.reporting;

import at.aau.cleancode.models.Page;
import at.aau.cleancode.models.textelements.LinkElement;
import at.aau.cleancode.models.textelements.TextElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class MarkDownReportGeneratorTest {
    private static final String LF = "\n";
    private ByteArrayOutputStream outputStream;
    private MarkDownReportGenerator generator;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        generator = new MarkDownReportGenerator(writer);
    }

    @Test
    void shouldGenerateMarkdownForPageWithHeaderAndLiveLink() throws IOException {
        // Arrange
        Page page = createPage(
                "http://example.com",
                3,
                List.of(
                        createTextElement("h2", "My Title"),
                        createLink(2, false, "http://link.com")
                )
        );

        // Act
        writeReportFor(page);

        // Assert
        String expected =
                "input: <a>http://example.com</a>" + LF +
                        "<br/>depth: 3" + LF +
                        "## My Title" + LF +
                        "<br>--> link to <a>http://link.com</a>" + LF +
                        LF;
        assertEquals(expected, getReport());
    }

    @Test
    void shouldGenerateMarkdownForPageWithDeadLinkOnly() throws IOException {
        // Arrange
        Page page = createPage(
                "https://foo.bar",
                0,
                List.of(
                        createTextElement("span", "ignored"),
                        createLink(1, true, "https://dead.link")
                )
        );

        // Act
        writeReportFor(page);

        // Assert
        String expected =
                "input: <a>https://foo.bar</a>" + LF +
                        "<br/>depth: 0" + LF +
                        "<br>-> [dead link] to <a>https://dead.link</a>" + LF +
                        LF;
        assertEquals(expected, getReport());
    }

    //helper methods
    private void writeReportFor(Page page) throws IOException {
        generator.writeFormattedReportToOutputWriter(List.of(page));
        flushBuffer();
    }

    private String getReport() {
        return outputStream.toString(StandardCharsets.UTF_8);
    }

    private void flushBuffer() {
        try {
            Field writerField = ReportGenerator.class.getDeclaredField("outputWriter");
            writerField.setAccessible(true);
            ((BufferedWriter) writerField.get(generator)).flush();
        } catch (ReflectiveOperationException | IOException ex) {
            throw new IllegalStateException("Could not flush internal buffer", ex);
        }
    }

    private Page createPage(String url, int depth, List<TextElement> elements) {
        Page page = mock(Page.class);
        given(page.getPageUrl()).willReturn(url);
        given(page.getPageCrawlDepth()).willReturn(depth);
        given(page.getTextElements()).willReturn(elements);
        return page;
    }

    private TextElement createTextElement(String tag, String text) {
        TextElement elem = mock(TextElement.class);
        given(elem.getElementName()).willReturn(tag);
        given(elem.getTextContent()).willReturn(text);
        return elem;
    }

    private LinkElement createLink(int elementDepth, boolean isDead, String href) {
        LinkElement link = mock(LinkElement.class);
        given(link.getElementName()).willReturn("a");
        given(link.getElementDepth()).willReturn(elementDepth);
        given(link.isDeadLink()).willReturn(isDead);
        given(link.getHref()).willReturn(href);
        return link;
    }
}