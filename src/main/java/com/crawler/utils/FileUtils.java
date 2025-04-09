package com.crawler.utils;

import com.crawler.domains.scanner.exceptions.InvalidDocumentFormatException;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class FileUtils {

    private final Tika tika;
    private final int BUFFER_SIZE = 1024;

    public byte[] readInputStreamInChunks(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] data = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, BUFFER_SIZE)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            return buffer.toByteArray();
        }
    }

    public void validateFileType(byte[] fileBytes, String expectedMimeType) {
        String mimeType = tika.detect(fileBytes);
        if (!MimeTypes.OCTET_STREAM.equals(mimeType) && !expectedMimeType.equals(mimeType)) {
            throw new InvalidDocumentFormatException("The file is not a valid, expected MIME type: " + expectedMimeType + ". Detected MIME type: " + mimeType);
        }
    }

    public String extractTextFromPdf(byte[] pdfBytes) throws IOException {
        validateFileType(pdfBytes, "application/pdf");
        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
}