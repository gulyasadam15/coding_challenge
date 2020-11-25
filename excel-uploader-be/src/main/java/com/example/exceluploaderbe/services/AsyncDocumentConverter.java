package com.example.exceluploaderbe.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncDocumentConverter {
    @Async
    public void runFakeConvertionOnDocument(Path currentPath, String docId) throws InterruptedException, IOException {
        Thread.sleep(10000);
        currentPath = Files.move(currentPath, currentPath.resolveSibling(docId + "_CONVERTED"));
    }
}
