package com.crawler.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PageCrawlerUtilsTest {

    private PageCrawlerUtils pageCrawlerUtils;

    @BeforeEach
    public void setUp() {
        pageCrawlerUtils = new PageCrawlerUtils();
    }

    @Test
    public void testExtractLinksFromPage_withValidLinks() throws IOException {
        String url = "http://example.com";
        Set<String> analyzedLinks = new HashSet<>();
        Document mockDocument = mock(Document.class);
        Connection mockConnection = mock(Connection.class);

        Elements mainElements = Jsoup.parse("<main><a href='http://example.com/link1'>Link1</a></main>").select("main, article, section");
        Elements allLinks = Jsoup.parse("<main><a href='http://example.com/link1'>Link1</a></main><a href='http://example.com/link2'>Link2</a>").select("a[href]");

        when(mockDocument.select("main, article, section")).thenReturn(mainElements);
        when(mockDocument.select("a[href]")).thenReturn(allLinks);
        when(mockConnection.get()).thenReturn(mockDocument);

        try (var jsoupMock = mockStatic(Jsoup.class)) {
            jsoupMock.when(() -> Jsoup.connect(url)).thenReturn(mockConnection);

            Set<String> result = pageCrawlerUtils.extractLinksFromPage(url, analyzedLinks);

            // Ensure both links are included
            Set<String> expectedLinks = Set.of("http://example.com/link1", "http://example.com/link2");
            assertEquals(expectedLinks, result);
        }
    }

    @Test
    public void testExtractLinks_withAnalyzedLinks() {
        Elements elements = Jsoup.parse("<a href='http://example.com/link1'>Link1</a><a href='http://example.com/link2'>Link2</a>").select("a[href]");
        Set<String> analyzedLinks = Set.of("http://example.com/link1");

        Set<String> result = pageCrawlerUtils.extractLinks(elements, analyzedLinks);

        Set<String> expectedLinks = Set.of("http://example.com/link2");
        assertEquals(expectedLinks, result);
    }

    @Test
    public void testExtractLinks_withNoNewLinks() {
        Elements elements = Jsoup.parse("<a href='http://example.com/link1'>Link1</a>").select("a[href]");
        Set<String> analyzedLinks = Set.of("http://example.com/link1");

        Set<String> result = pageCrawlerUtils.extractLinks(elements, analyzedLinks);

        assertEquals(Set.of(), result);
    }
}