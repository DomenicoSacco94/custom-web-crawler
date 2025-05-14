package com.crawler.utils;

import com.crawler.domains.scanner.exceptions.InvalidUrlException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class DownloadUtils {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?|ftp)://[\\w.-]+(:\\d+)?(/.*)?$", Pattern.CASE_INSENSITIVE);

    private static final List<String> supportedContentTypes = List.of("html", "pdf", "xml", "text");

    private final FileUtils fileUtils;

    public String downloadAndExtractText(String fileUrl) throws IOException {
        if (!isValidUrl(fileUrl)) {
            throw new InvalidUrlException("Invalid URL: " + fileUrl);
        }

        log.info("Downloading: {}", fileUrl);

        URL url = new URL(fileUrl);
        URLConnection connection = url.openConnection();
        String contentType = connection.getContentType();

        if (contentType == null || supportedContentTypes.stream().noneMatch(contentType::contains)) {
            throw new InvalidUrlException("Unsupported content type: " + contentType);
        }

        if (contentType.contains("pdf")) {
            try (InputStream inputStream = connection.getInputStream()) {
                byte[] pdfBytes = fileUtils.readInputStreamInChunks(inputStream);
                return fileUtils.extractTextFromPdf(pdfBytes);
            }
        } else {
            return extractTextFromWebPage(fileUrl);
        }
    }


    private String extractTextFromWebPage(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return document.text();
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url);
            return URL_PATTERN.matcher(url).matches();
        } catch (MalformedURLException e) {
            return false;
        }
    }
}