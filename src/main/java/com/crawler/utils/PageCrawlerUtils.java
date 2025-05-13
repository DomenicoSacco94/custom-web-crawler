package com.crawler.utils;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PageCrawlerUtils {

    private static final int MAX_LINKS_PER_DOCUMENT = 5;

    public Set<String> extractLinksFromPage(String url, Set<String> analyzedLinks) throws IOException {
        Document document = Jsoup.connect(url).get();

        // Extract links from meaningful sections
        Set<String> newLinks = new HashSet<>(extractLinks(document.select("main, article, section"), analyzedLinks));

        // Fallback: Extract links from the entire document if needed
        if (newLinks.size() < MAX_LINKS_PER_DOCUMENT) {
            newLinks.addAll(extractLinks(document.select("a[href]"), analyzedLinks));
        }

        return newLinks.stream()
                .limit(MAX_LINKS_PER_DOCUMENT)
                .collect(Collectors.toSet());
    }

    private Set<String> extractLinks(Elements elements, Set<String> analyzedLinks) {
        Set<String> links = new HashSet<>();
        for (Element element : elements) {
            String link = element.attr("abs:href");

            // Check if the link is already analyzed
            if (!analyzedLinks.contains(link)) {
                links.add(link);
                if (links.size() >= MAX_LINKS_PER_DOCUMENT) {
                    return links;
                }
            }
        }
        return links;
    }
}