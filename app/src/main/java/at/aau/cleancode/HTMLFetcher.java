package at.aau.cleancode;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLFetcher { 
    public Document fetch(String url) throws IOException {
            return Jsoup.connect(url).get();
    }
}
