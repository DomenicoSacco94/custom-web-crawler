package com.crawler.domains.scanner;

import com.crawler.domains.scanner.exceptions.InvalidDocumentUrlException;
import com.crawler.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
@Slf4j
public class DocumentDownloadService {

    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?|ftp)://[\\w.-]+(:\\d+)?(/([\\w/_.-]*)?)?$", Pattern.CASE_INSENSITIVE);

    private final FileUtils fileUtils;

    public byte[] downloadFile(String fileUrl) throws IOException {
        if (!isValidUrl(fileUrl)) {
            throw new InvalidDocumentUrlException("Invalid URL: " + fileUrl);
        }

        log.info("Downloading document from : {}", fileUrl);

        try (InputStream inputStream = new URL(fileUrl).openStream()) {
            return fileUtils.readInputStreamInChunks(inputStream);
        }
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