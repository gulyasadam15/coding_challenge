package com.example.exceluploaderbe.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import com.example.exceluploaderbe.dtos.DocumentContent;
import com.example.exceluploaderbe.dtos.DocumentContent.DocumentRow;
import com.example.exceluploaderbe.dtos.DocumentProcessingState.ProcessingState;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DocumentService {
    private final AsyncDocumentConverter documentConverter;

    public ProcessingState getProcessingState(String documentId) {
        File document = findFileByDocumentId(documentId);
        String stateAlias = document.getName().split("_")[1];
        return ProcessingState.valueOf(stateAlias);
    }

    public DocumentContent getDocumentContent(String documentId) {
        File document = findFileByDocumentId(documentId);
        DocumentContent content = new DocumentContent();
        content.setDocument(new LinkedList<>());
        try {
            FileInputStream excelFile = new FileInputStream(document);
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            // skip header
            iterator.next();

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                DocumentRow documentRow = new DocumentRow();
                documentRow.setPassengerClass(getStringValue(currentRow.getCell(0)));
                documentRow.setName(getStringValue(currentRow.getCell(1)));
                documentRow.setSex(getStringValue(currentRow.getCell(2)));
                documentRow.setAge(getIntValue(currentRow.getCell(3)));
                documentRow.setNoOfSiblingsOrSpousesOnBoard(getIntValue(currentRow.getCell(4)));
                documentRow.setNoOfParentsOrChildrenOnBoard(getIntValue(currentRow.getCell(5)));
                documentRow.setTicketNumber(getStringValue(currentRow.getCell(6)));
                documentRow.setPassengerFare(getLongValue(currentRow.getCell(7)));
                documentRow.setCabin(getStringValue(currentRow.getCell(8)));
                documentRow.setPortOfEmbarkation(getStringValue(currentRow.getCell(9)));
                documentRow.setLifeBoat(getStringValue(currentRow.getCell(10)));
                documentRow.setSurvived(getStringValue(currentRow.getCell(11)));
                content.getDocument().add(documentRow);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public String uploadDocument(MultipartFile document) throws IOException, InterruptedException {
        String baseIdentifier = UUID.randomUUID().toString();
        Path path = Paths.get("uploadedFiles/" + baseIdentifier + "_CONVERTING");
        Path parentDir = path.getParent();
        if (!Files.exists(parentDir)){
            Files.createDirectories(parentDir);
        }
        Files.write(path, document.getBytes());
        documentConverter.runFakeConvertionOnDocument(path,baseIdentifier);
        return baseIdentifier;
    }


    private File findFileByDocumentId(String documentId) {
        Path parentDir = Paths.get("uploadedFiles");
        return parentDir.toFile().listFiles((dir, name) -> name.contains(documentId))[0];
    }

    private String getStringValue(Cell cell) {
        if(cell == null){
            return null;
        }
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            return String.valueOf(cell.getNumericCellValue());
        }
        return cell.getStringCellValue();
    }

    private Long getLongValue(Cell cell) {
        if(cell!=null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            return (long) cell.getNumericCellValue();
        }
        return null;
    }

    private Integer getIntValue(Cell cell) {
        if(cell!=null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            return (int) cell.getNumericCellValue();
        }
        return null;
    }

    
}