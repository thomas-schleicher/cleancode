package at.aau.cleancode.fetching;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsoupFetcherTest {
    private static final String TEST_URL = "http://example.com";
    private static final String HTML_CONTENT = "<html><body>Hello</body></html>";
    private JsoupFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new JsoupFetcher();
    }

    private Connection createMockConnection(int statusCode, String body) throws IOException {
        Connection conn = mock(Connection.class);
        Connection.Response resp = mock(Connection.Response.class);

        when(resp.statusCode()).thenReturn(statusCode);
        when(resp.body()).thenReturn(body);

        when(conn.ignoreHttpErrors(true)).thenReturn(conn);
        when(conn.timeout(5000)).thenReturn(conn);
        when(conn.followRedirects(true)).thenReturn(conn);
        when(conn.ignoreContentType(true)).thenReturn(conn);
        when(conn.execute()).thenReturn(resp);

        return conn;
    }

    @Test
    void shouldReturnDocumentWhenHttpStatusIs2xx() throws IOException {
        Connection fakeConn = createMockConnection(200, HTML_CONTENT);
        Document realDoc = Jsoup.parse(HTML_CONTENT, TEST_URL);

        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            jsoupMock.when(() -> Jsoup.connect(TEST_URL)).thenReturn(fakeConn);
            jsoupMock.when(() -> Jsoup.parse(HTML_CONTENT, TEST_URL)).thenReturn(realDoc);

            Document doc = fetcher.getRawPage(TEST_URL);

            assertNotNull(doc, "Document must not be null on 2xx");
            assertEquals(TEST_URL, doc.baseUri(), "Base URI should be the request URL");
            assertTrue(doc.html().contains("Hello"), "Body content must include the fetched HTML");
        }
    }

    @Test
    void shouldThrowIOExceptionWhenHttpStatusIsNot2xx() throws IOException {
        Connection mockConn = createMockConnection(404, "");

        try (MockedStatic<Jsoup> jsoup = mockStatic(Jsoup.class)) {
            jsoup.when(() -> Jsoup.connect(TEST_URL)).thenReturn(mockConn);

            IOException ex = assertThrows(IOException.class,
                    () -> fetcher.getRawPage(TEST_URL),
                    "Non-2xx should cause IOException");

            assertTrue(ex.getMessage().contains(TEST_URL),
                    "Error message should mention the URL");
            assertTrue(ex.getMessage().contains("404"),
                    "Error message should include the status code");
        }
    }
}
