package com.example.exceluploaderbe.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AsyncDocumentConverterTest {
    public static String MOCK_DOCUMENT_ID = "aqsfsdffasf";
    private static Path path = Paths.get("uploadedFiles/" + MOCK_DOCUMENT_ID + "_CONVERTING");

    @BeforeClass
    public static void setup() throws IOException {
        Path parentDir = path.getParent();
        if (!Files.exists(parentDir)){
            Files.createDirectories(parentDir);
        }
        Files.write(path, "mockContent".getBytes());
    }

    @Test
    public void getProcessingStateTest() throws InterruptedException, IOException {
        AsyncDocumentConverter documentConverter = new AsyncDocumentConverter();
        documentConverter.runFakeConvertionOnDocument(path, MOCK_DOCUMENT_ID);
        Thread.sleep(11*1000);
        assertFalse(Files.exists(path));
        assertTrue(Files.exists(Paths.get("uploadedFiles/" + MOCK_DOCUMENT_ID + "_CONVERTED")));
    }

    @AfterClass
    public static void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get("uploadedFiles/" + MOCK_DOCUMENT_ID + "_CONVERTING"));
        Files.deleteIfExists(Paths.get("uploadedFiles/" + MOCK_DOCUMENT_ID + "_CONVERTED"));
    }
}
