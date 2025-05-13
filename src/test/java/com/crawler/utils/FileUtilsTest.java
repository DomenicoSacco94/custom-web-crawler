package com.crawler.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class FileUtilsTest {

    private FileUtils fileUtils;

    @BeforeEach
    public void setUp() {
        fileUtils = new FileUtils();
    }

    @Test
    public void testReadInputStreamInChunks() throws IOException {
        String testData = "This is a test string";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        byte[] result = fileUtils.readInputStreamInChunks(inputStream);

        assertArrayEquals(testData.getBytes(), result);
    }

    @Test
    public void testReadInputStreamInChunks_emptyStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        byte[] result = fileUtils.readInputStreamInChunks(inputStream);

        assertArrayEquals(new byte[0], result);
    }

    @Test
    public void testValidateFileTypeWithValidMimeType() throws Exception {
        Path validFilePath = Paths.get("src/test/resources/testfiles/Testdata_Invoices.pdf");
        byte[] validFileContent = Files.readAllBytes(validFilePath);

        assertDoesNotThrow(() ->
            fileUtils.extractTextFromPdf(validFileContent));
    }
}