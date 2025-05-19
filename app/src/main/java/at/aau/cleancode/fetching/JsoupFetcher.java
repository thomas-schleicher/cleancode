package at.aau.cleancode.fetching;

import at.aau.cleancode.parsing.JsoupParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsoupFetcher extends HTMLFetcher<Document> {

    private static final Logger LOGGER = Logger.getLogger(JsoupFetcher.class.getName());

    public JsoupFetcher() {
        super(new JsoupParser());
    }

    @Override
    Document getRawPage(String url) throws IOException {
        Connection.Response response = Jsoup.connect(url)
                .ignoreHttpErrors(true)
                .followRedirects(true)
                .ignoreContentType(true)
                .execute();

        int statusCode = response.statusCode();
        if (statusCode >= 200 && statusCode < 300) {
            return Jsoup.parse(response.body(), url);
        } else {
            LOGGER.log(Level.WARNING, "Failed to fetch page: {0}", url);
            throw new IOException("Failed to fetch URL: " + url + " (status " + statusCode + ")");
        }
    }
}