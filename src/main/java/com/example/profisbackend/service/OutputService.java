package com.example.profisbackend.service;

import com.example.profisbackend.entities.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OutputService {

    private final ScientificWorkService scientificWorkService;
    private final StudyProgramService studyProgramService;

    public ResponseEntity<byte[]> exportDatabaseToExcel() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet workSheet = workbook.createSheet("Scientific Works");
            createWorkHeader(workSheet);
            fillWorkData(workSheet);

            for (int i = 0; i <= 41; i++) {
                workSheet.autoSizeColumn(i);
            }

            Sheet programSheet = workbook.createSheet("Study Programs");
            createProgramHeader(programSheet);
            fillProgramData(programSheet);
            for (int i = 0; i <= 2; i++) programSheet.autoSizeColumn(i);

            workbook.write(out);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Scientific_Works_Export.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(out.toByteArray());
        }
    }

    private void createWorkHeader(Sheet sheet) {
        Row header = sheet.createRow(0);
        CellStyle style = createHeaderStyle(sheet.getWorkbook());

        addCell(header, 0, "Status", style);
        addCell(header, 1, "Erstprüfer Anrede", style);
        addCell(header, 2, "Erstprüfer Vorname", style);
        addCell(header, 3, "Erstprüfer Nachname", style);
        addCell(header, 4, "Erstprüfer Abschluss", style);
        addCell(header, 5, "Erstprüfer Rolle", style);
        addCell(header, 6, "Erstprüfer Adresse", style);
        addCell(header, 7, "Erstprüfer E-Mail", style);
        addCell(header, 8, "Erstprüfer Telefon", style);

        addCell(header, 9, "Zweitprüfer Anrede", style);
        addCell(header, 10, "Zweitprüfer Vorname", style);
        addCell(header, 11, "Zweitprüfer Nachname", style);
        addCell(header, 12, "Zweitprüfer Abschluss", style);
        addCell(header, 13, "Zweitprüfer Rolle", style);
        addCell(header, 14, "Zweitprüfer Adresse", style);
        addCell(header, 15, "Zweitprüfer E-Mail", style);
        addCell(header, 16, "Zweitprüfer Telefon", style);

        addCell(header, 17, "Vorname Stud.", style);
        addCell(header, 18, "Nachname Stud.", style);
        addCell(header, 19, "Abschluss Stud.", style);
        addCell(header, 20, "Anrede", style);
        addCell(header, 21, "Telefonnummer", style);
        addCell(header, 22, "E-Mail Stud.", style);
        addCell(header, 23, "Matrikelnummer", style);
        addCell(header, 24, "Adresse Stud.", style);

        addCell(header, 25, "Studiengang Level", style);
        addCell(header, 26, "Studiengang Name", style);

        addCell(header, 27, "Titel der Arbeit", style);
        addCell(header, 28, "Startdatum", style);
        addCell(header, 29, "Abgabedatum", style);
        addCell(header, 30, "Kolloquium Datum", style);
        addCell(header, 31, "Kolloquium Ort", style);
        addCell(header, 32, "Präsentat. Start", style);
        addCell(header, 33, "Präsentat. Ende", style);
        addCell(header, 34, "Diskussions Start", style);
        addCell(header, 35, "Diskussions Ende", style);

        addCell(header, 36, "Erstprüfer Note Arbeit", style);
        addCell(header, 37, "Erstprüfer Note Kolloq.", style);
        addCell(header, 38, "Zweitprüfer Note Arbeit", style);
        addCell(header, 39, "Zweitprüfer Note Kolloq.", style);

        addCell(header, 40, "Deputat (SWS)", style);
        addCell(header, 41, "Kommentar", style);
    }

    private void fillWorkData(Sheet sheet) {
        List<ScientificWork> works = scientificWorkService.findAll();
        int rowIdx = 1;

        for (ScientificWork work : works) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Angemeldet");

            if (work.getMainEvaluator() != null) {
                Evaluator me = work.getMainEvaluator();
                row.createCell(1).setCellValue(me.getSalutation().getLabel());
                row.createCell(2).setCellValue(me.getFirstName());
                row.createCell(3).setCellValue(me.getLastName());
                row.createCell(4).setCellValue(me.getAcademicLevel().getLabel());
                row.createCell(5).setCellValue(me.getRole().getLabel());
                row.createCell(6).setCellValue(me.getAddress());
                row.createCell(7).setCellValue(me.getEmail());
                row.createCell(8).setCellValue(me.getPhoneNumber());
            }

            if (work.getSecondEvaluator() != null) {
                Evaluator se = work.getSecondEvaluator();
                row.createCell(9).setCellValue(se.getSalutation().getLabel());
                row.createCell(10).setCellValue(se.getFirstName());
                row.createCell(11).setCellValue(se.getLastName());
                row.createCell(12).setCellValue(se.getAcademicLevel().getLabel());
                row.createCell(13).setCellValue(se.getRole().getLabel());
                row.createCell(14).setCellValue(se.getAddress());
                row.createCell(15).setCellValue(se.getEmail());
                row.createCell(16).setCellValue(se.getPhoneNumber());
            }

            if (work.getStudent() != null) {
                Student s = work.getStudent();
                row.createCell(17).setCellValue(s.getFirstName());
                row.createCell(18).setCellValue(s.getLastName());
                row.createCell(19).setCellValue(s.getAcademicLevel().getLabel());
                row.createCell(20).setCellValue(s.getSalutation().getLabel());
                row.createCell(21).setCellValue(s.getPhoneNumber());
                row.createCell(22).setCellValue(s.getEmail());
                row.createCell(23).setCellValue(s.getStudentNumber());
                row.createCell(24).setCellValue(s.getAddress());
            }

            if (work.getStudyProgram() != null) {
                row.createCell(25).setCellValue(work.getStudyProgram().getDegreeType().getLabel());
                row.createCell(26).setCellValue(work.getStudyProgram().getTitle());
            }

            row.createCell(27).setCellValue(work.getTitle());
            row.createCell(28).setCellValue(work.getStartDate() != null ? work.getStartDate().toString() : "");
            row.createCell(29).setCellValue(work.getEndDate() != null ? work.getEndDate().toString() : "");
            row.createCell(30).setCellValue(work.getColloquium() != null ? work.getColloquium().toString() : "");
            row.createCell(31).setCellValue(work.getColloquiumLocation());
            row.createCell(32).setCellValue(work.getPresentationStart() != null ? work.getPresentationStart().toString() : "");
            row.createCell(33).setCellValue(work.getPresentationEnd() != null ? work.getPresentationEnd().toString() : "");
            row.createCell(34).setCellValue(work.getDiscussionStart() != null ? work.getDiscussionStart().toString() : "");
            row.createCell(35).setCellValue(work.getDiscussionEnd() != null ? work.getDiscussionEnd().toString() : "");

            row.createCell(36).setCellValue(work.getMainEvaluatorWorkMark());
            row.createCell(37).setCellValue(work.getMainEvaluatorColloquiumMark());
            row.createCell(38).setCellValue(work.getSecondEvaluatorWorkMark());
            row.createCell(39).setCellValue(work.getSecondEvaluatorColloquiumMark());

            row.createCell(40).setCellValue(0.5);
            row.createCell(41).setCellValue(work.getComment());
        }
    }

    private void fillProgramData(Sheet sheet) {
        List<StudyProgram> programs = studyProgramService.findAll();
        int rowIdx = 1;
        for (StudyProgram sp : programs) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(sp.getDegreeType().getLabel());
            row.createCell(1).setCellValue(sp.getTitle());
            row.createCell(2).setCellValue(sp.getSws());
        }
    }

    private void createProgramHeader(Sheet sheet) {
        Row header = sheet.createRow(0);
        CellStyle style = createHeaderStyle(sheet.getWorkbook());
        addCell(header, 0, "Abschluss", style);
        addCell(header, 1, "Studiengang", style);
        addCell(header, 2, "SWS", style);
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private void addCell(Row row, int index, String value, CellStyle style) {
        Cell cell = row.createCell(index);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
}