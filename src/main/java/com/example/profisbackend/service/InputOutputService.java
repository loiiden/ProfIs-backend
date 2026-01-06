package com.example.profisbackend.service;

import java.io.FileInputStream;
import java.net.ResponseCache;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.profisbackend.dto.studyprogram.StudyProgramDTO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class InputOutputService {
    public static final int STUDY_PROGRAM_SHEET=1;
    public static final int STUDY_PROGRAM_TITLE_CELL=3;

    private final StudyProgramService studyProgramService;

    private XSSFWorkbook createWorkBook(String path) {
        try {
            FileInputStream file = new FileInputStream(path);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            return workbook;
        } catch (Exception e) {
            System.out.println("File not found" + e.getCause());
        }
        return null;
    }

    private void persistStudyPrgram(XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.getSheetAt(STUDY_PROGRAM_SHEET);
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell title = row.getCell(STUDY_PROGRAM_TITLE_CELL);
            StudyProgramDTO studyProgramDTO = new StudyProgramDTO(title.getStringCellValue(), 1.1f); // sws only for filling the dto, logic willl be changed in the next upcoming push
            studyProgramService.createStudyProgram(studyProgramDTO);
        }
    }

    public ResponseEntity<String> readInputFile(String path) {
        persistStudyPrgram(createWorkBook(path));
        return ResponseEntity.ok("Study Program has been created by the submitted excel file");
    }
}
