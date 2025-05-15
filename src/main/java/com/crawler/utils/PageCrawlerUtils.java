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

    private static final int MAX_LINKS_PER_PAGE = 5;

    public Set<String> extractLinksFromPage(String url, Set<String> analyzedLinks) throws IOException {
        Document page = Jsoup.connect(url).get();

        // Extract links from meaningful sections
        Set<String> newLinks = new HashSet<>(extractLinks(page.select("main, article, section"), analyzedLinks));

        // Extract links from the entire document if not enough links are found
        if (newLinks.size() < MAX_LINKS_PER_PAGE) {
            newLinks.addAll(extractLinks(page.select("a[href]"), analyzedLinks));
        }

        return newLinks.stream()
                .limit(MAX_LINKS_PER_PAGE)
                .collect(Collectors.toSet());
    }

    public Set<String> extractLinks(Elements elements, Set<String> analyzedLinks) {
        Set<String> links = new HashSet<>();
        for (Element element : elements) {
            String link = element.attr("abs:href");

            if (!analyzedLinks.contains(link) && !link.isEmpty()) {
                links.add(link);
                if (links.size() >= MAX_LINKS_PER_PAGE) {
                    return links;
                }
            }
        }
        return links;
    }
}