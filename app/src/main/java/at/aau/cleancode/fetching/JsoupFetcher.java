package at.aau.cleancode.fetching;

import java.io.IOException;

import at.aau.cleancode.parsing.JsoupParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupFetcher extends PageFetcher<Document> {

    public JsoupFetcher() {
        super(new JsoupParser());
    }

    @Override
    Document getRawPage(String url) throws IOException {
        Connection.Response response = Jsoup.connect(url)
                .ignoreHttpErrors(true)
                .timeout(5000)
                .execute();

        int statusCode = response.statusCode();
        if (statusCode >= 200 && statusCode < 300) {
            return Jsoup.parse(response.body());
        } else {
            throw new IOException("Failed to fetch URL: " + url + " (status " + statusCode + ")");
        }
    }
}