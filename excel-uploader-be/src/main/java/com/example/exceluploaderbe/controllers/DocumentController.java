package com.example.exceluploaderbe.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

import com.example.exceluploaderbe.dtos.DocumentContent;
import com.example.exceluploaderbe.dtos.DocumentProcessingState;
import com.example.exceluploaderbe.dtos.DocumentProcessingState.ProcessingState;
import com.example.exceluploaderbe.services.DocumentService;

import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("documentProcessingState/{documentId}")
    public DocumentProcessingState getProcessingState(@PathVariable("documentId") String documentId) {
        return new DocumentProcessingState(documentId,documentService.getProcessingState(documentId));
    }

    @GetMapping("documentContent/{documentId}")
    public DocumentContent getDocumentContent(@PathVariable("documentId") String documentId) {
        return documentService.getDocumentContent(documentId);
    }

    @PostMapping(value="upload")
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentProcessingState uploadDocument(@RequestParam("file") MultipartFile document) throws IOException,
            InterruptedException {
        String documentId = documentService.uploadDocument(document);
        return new DocumentProcessingState(documentId,ProcessingState.CONVERTING);
    }

}