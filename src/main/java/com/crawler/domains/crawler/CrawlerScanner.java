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

    private static final int MAX_LINKS = 10;
    private final Set<String> analyzedLinks = new HashSet<>();

    public Set<String> extractLinksFromPage(String url) throws IOException {
        Set<String> newLinks = new HashSet<>();
        Document document = Jsoup.connect(url).get();

        // Select meaningful sections (e.g., main content)
        Elements meaningfulSections = document.select("main, article, section");

        for (Element section : meaningfulSections) {
            Elements anchorTags = section.select("a[href]");
            for (Element anchor : anchorTags) {
                String link = anchor.attr("abs:href");

                // Check if the link is already analyzed
                if (!analyzedLinks.contains(link)) {
                    newLinks.add(link);
                    if (newLinks.size() >= MAX_LINKS) {
                        return newLinks;
                    }
                }
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