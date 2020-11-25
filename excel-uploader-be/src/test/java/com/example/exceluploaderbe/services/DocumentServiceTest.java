package com.example.exceluploaderbe.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.exceluploaderbe.dtos.DocumentProcessingState.ProcessingState;

import org.apache.poi.POIXMLException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

public class DocumentServiceTest {
    public static String MOCK_DOCUMENT_ID = "aqsfsdffasf";
    private static Path path = Paths.get("uploadedFiles/" + MOCK_DOCUMENT_ID + "_CONVERTED");


    @Mock
    AsyncDocumentConverter documentConverter;

    @BeforeClass
    public static void setup() throws IOException {
        Path parentDir = path.getParent();
        if (!Files.exists(parentDir)){
            Files.createDirectories(parentDir);
        }
        Files.write(path, "mockContent".getBytes());
    }

    @Test
    public void getProcessingStateTest(){
        DocumentService documentService = new DocumentService(documentConverter);
        ProcessingState state = documentService.getProcessingState(MOCK_DOCUMENT_ID);
        assertEquals(ProcessingState.CONVERTED,state);
    }

    @Test(expected=POIXMLException.class)
    public void getDocumentContentTest(){
        DocumentService documentService = new DocumentService(documentConverter);
        documentService.getDocumentContent(MOCK_DOCUMENT_ID);
    }

    @AfterClass
    public static void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get("uploadedFiles/" + MOCK_DOCUMENT_ID + "_CONVERTED"));
    }
}
