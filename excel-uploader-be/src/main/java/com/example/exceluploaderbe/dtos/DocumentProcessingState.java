package com.example.exceluploaderbe.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DocumentProcessingState{
    private String documentId;
    private ProcessingState documentProcessingState;

    public enum ProcessingState{
        UPLOADING, 
        UPLOAD_ERROR, 
        CONVERTING,
        CONVERSATION_ERROR, 
        CONVERTED
    }
}