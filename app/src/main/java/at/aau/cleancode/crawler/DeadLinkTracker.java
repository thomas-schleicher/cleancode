package at.aau.cleancode.crawler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DeadLinkTracker {

    private final Set<String> deadLinks = ConcurrentHashMap.newKeySet();

    public void addDeadLink(String url) {
        deadLinks.add(url);
    }

    public Set<String> getDeadLinks() {
        return deadLinks;
    }
}
