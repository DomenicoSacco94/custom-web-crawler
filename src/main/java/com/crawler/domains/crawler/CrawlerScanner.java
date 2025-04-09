package com.crawler.domains.crawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class CrawlerScanner {

    private final Set<String> analyzedLinks = new HashSet<>();

    public Set<String> extractLinksFromPage(String url) throws IOException {
        Set<String> newLinks = new HashSet<>();
        Document document = Jsoup.connect(url).get();
        Elements anchorTags = document.select("a[href]");

        for (Element anchor : anchorTags) {
            String link = anchor.attr("abs:href"); // Get absolute URL
            if (!analyzedLinks.contains(link)) {
                newLinks.add(link);
            }
        }
        return newLinks;
    }

    public boolean isLinkAnalyzed(String url) {
        return analyzedLinks.contains(url);
    }

    public void markLinkAsAnalyzed(String url) {
        analyzedLinks.add(url);
    }
}