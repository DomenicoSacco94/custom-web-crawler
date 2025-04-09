package com.crawler.domains.scanner;

import com.crawler.domains.scanner.exceptions.InvalidDocumentUrlException;
import com.crawler.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
@Slf4j
public class DocumentDownloadService {

    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?|ftp)://[\\w.-]+(:\\d+)?(/([\\w/_.-]*)?)?$", Pattern.CASE_INSENSITIVE);

    private final FileUtils fileUtils;

    public String downloadAndExtractText(String fileUrl) throws IOException {
        if (!isValidUrl(fileUrl)) {
            throw new InvalidDocumentUrlException("Invalid URL: " + fileUrl);
        }

        log.info("Downloading document from: {}", fileUrl);

        URL url = new URL(fileUrl);
        URLConnection connection = url.openConnection();
        String contentType = connection.getContentType();

        if (contentType != null && contentType.contains("pdf")) {
            try (InputStream inputStream = connection.getInputStream()) {
                byte[] pdfBytes = fileUtils.readInputStreamInChunks(inputStream);
                return fileUtils.extractTextFromPdf(pdfBytes);
            }
        } else if (contentType != null && contentType.contains("html")) {
            return extractTextFromWebPage(fileUrl);
        } else {
            throw new InvalidDocumentUrlException("Unsupported content type: " + contentType);
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