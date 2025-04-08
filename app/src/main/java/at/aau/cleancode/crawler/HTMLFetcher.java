package at.aau.cleancode.crawler;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLFetcher {
    //TODO: maybe include an adapter patter for this in the second sprint (a lot of refactoring) :(
    public Document fetch(String url) throws IOException {
        Connection.Response response = Jsoup.connect(url)
                .ignoreHttpErrors(true)
                .timeout(5000)
                .execute();

        int statusCode = response.statusCode();
        if (statusCode >= 200 && statusCode < 300) {
            return response.parse();
        } else {
            throw new IOException("Failed to fetch URL: " + url + " (status " + statusCode + ")");
        }
    }
}
