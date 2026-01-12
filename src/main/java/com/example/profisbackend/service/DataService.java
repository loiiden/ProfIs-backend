package com.example.profisbackend.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.example.profisbackend.dto.evaluator.EvaluatorCreateDTO;
import com.example.profisbackend.dto.evaluator.EvaluatorPatchDTO;
import com.example.profisbackend.dto.event.EventCreateDTO;
import com.example.profisbackend.dto.event.EventPatchDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkCreateDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkPatchDTO;
import com.example.profisbackend.dto.student.StudentCreateDTO;
import com.example.profisbackend.dto.student.StudentPatchDTO;
import com.example.profisbackend.entities.*;
import com.example.profisbackend.enums.*;
import com.example.profisbackend.dto.studyprogram.StudyProgramDTO;
import com.example.profisbackend.mapper.StudyProgramMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import static com.example.profisbackend.config.Constants.*;

@RequiredArgsConstructor
@Service
public class DataService {

    private static final Logger log = LoggerFactory.getLogger(DataService.class);

    private final StudyProgramService studyProgramService;
    private final StudentService studentService;
    private final ScientificWorkService scientificWorkService;
    private final EvaluatorService evaluatorService;
    private final EventService eventService;

    public ResponseEntity<String> copyToDb(MultipartFile excelFile) {
        try (InputStream file = excelFile.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(file)) {

            saveStudyProgramsFromExcel(workbook);
            saveScientificWorkFromExcel(workbook);

            return ResponseEntity.ok("Successfully imported Excel data to database");
        } catch (Exception e) {
            log.error("Global import failure: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error during import: " + e.getMessage());
        }
    }

    private void saveStudyProgramsFromExcel(XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.getSheetAt(STUDY_PROGRAM_SHEET);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String degreeLabel = getSafeString(row.getCell(DEGREE_COLUMN));
            String title = getSafeString(row.getCell(STUDY_PROGRAM_TITLE_COLUMN));
            double sws = (double) getSafeNumeric(row.getCell(SEMESTER_WEEKS_HOURS_COLUMN));

            if (!studyProgramService.existsByDegreeTypeAndTitle(DegreeType.valueOfLabel(degreeLabel), title)) {
                StudyProgramDTO dto = StudyProgramMapper.convertExcelToStudyProgramResponseDTO(degreeLabel, title, sws);
                studyProgramService.createStudyProgram(dto);
            }
        }
    }

    private void saveScientificWorkFromExcel(XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.getSheetAt(SCIENTIFIC_WORK_SHEET);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) continue;

            try {
                Long studyProgramId = getStudyProgram(row);
                Long studentId = upsertStudent(row, studyProgramId);

                Long mainEvalId = upsertEvaluator(row, FIRST_EVALUATOR_SALUTATION, FIRST_EVALUATOR_FIRSTNAME,
                        FIRST_EVALUATOR_LASTNAME, FIRST_EVALUATOR_ACADEMICLEVEL, FIRST_EVALUATOR_ROLE,
                        FIRST_EVALUATOR_ADDRESS, FIRST_EVALUATOR_EMAIL, FIRST_EVALUATOR_PHONE);

                Long secondEvalId = upsertEvaluator(row, SECOND_EVALUATOR_SALUTATION, SECOND_EVALUATOR_FIRSTNAME,
                        SECOND_EVALUATOR_LASTNAME, SECOND_EVALUATOR_ACADEMICLEVEL, SECOND_EVALUATOR_ROLE,
                        SECOND_EVALUATOR_ADDRESS, SECOND_EVALUATOR_EMAIL, SECOND_EVALUATOR_PHONE);

                Long scientificWorkId = upsertScientificWork(row, studentId, studyProgramId, mainEvalId, secondEvalId);
                upsertAllEvents(row, scientificWorkId);

            } catch (Exception e) {
                log.warn("Skipping row {}: {}", i, e.getMessage());
            }
        }
    }
    private void upsertAllEvents(Row row, Long scientificWorkId) {
        upsertPlannedEvent(row, scientificWorkId);
        /*
        upsertProposalEvent();
        upsertDiscussionPlannedEvent();
        upsertFinalSubmissionPlannedEvent();
        upsertReviewEvent();
        upsertArchiveEvent();
        upsertAbortEvent();
         */
    }
    private Long upsertPlannedEvent(Row row, Long scientificWorkId) {
        LocalDate eventDate = getSafeLocalDate(row.getCell(START_DATE_COLUMN), "StartDate");
        Optional<Event> foundEvent = eventService.findByEventTypeAndScientificWorkId(EventType.PLANNED, scientificWorkId);
        if (foundEvent.isEmpty()){
            EventCreateDTO eventCreateDTO = new EventCreateDTO(
                    EventType.PLANNED,
                    eventDate,
                    scientificWorkId
            );
            Event created = eventService.createEvent(eventCreateDTO);
            return created.getId();
        } else {
            EventPatchDTO eventPatchDTO = new EventPatchDTO(
                    EventType.PLANNED,
                    eventDate
            );
            Event updated = eventService.updateEvent(foundEvent.get().getId(), eventPatchDTO);
            return updated.getId();
        }
    }

    private Long upsertEvaluator(Row row, int sal, int fName, int lName, int level, int role, int addr, int email, int phone) {
        String mail = getSafeString(row.getCell(email));
        EvaluatorCreateDTO createDto = new EvaluatorCreateDTO(
                getSafeString(row.getCell(fName)), getSafeString(row.getCell(lName)),
                getSafeString(row.getCell(addr)), mail, getSafeString(row.getCell(phone)),
                AcademicLevel.valueOfLabel(getSafeString(row.getCell(level))),
                EvaluatorRole.valueOfLabel(getSafeString(row.getCell(role))),
                Salutation.valueOfLabel(getSafeString(row.getCell(sal)))
        );

        if (!evaluatorService.existsByEmail(mail)) {
            return evaluatorService.createEvaluator(createDto).getId();
        }

        Long id = evaluatorService.findByEmail(mail).getId();
        evaluatorService.patchEvaluator(id, new EvaluatorPatchDTO(
                createDto.firstName(), createDto.lastName(), createDto.address(),
                createDto.email(), createDto.phoneNumber(), createDto.academicLevel(),
                createDto.role(), createDto.salutation()));
        return id;
    }

    private Long upsertStudent(Row row, Long studyProgramId) {
        Long studentNumber = (long) getSafeNumeric(row.getCell(STUDENT_NUMBER_COLUMN));
        StudentCreateDTO createDto = new StudentCreateDTO(
                getSafeString(row.getCell(STUDENT_FIRSTNAME_COLUMN)),
                getSafeString(row.getCell(STUDENT_LASTNAME_COLUMN)),
                getSafeString(row.getCell(STUDENT_ADDRESS_COLUMN)),
                getSafeString(row.getCell(STUDENT_MAIL_COLUMN)),
                getSafeString(row.getCell(STUDENT_PHONENUMBER_COLUMN)),
                studentNumber,
                Salutation.valueOfLabel(getSafeString(row.getCell(STUDENT_SALUTATION_COLUMN))),
                AcademicLevel.valueOfLabel(getSafeString(row.getCell(STUDENT_ACADEMICLEVEL_COLUMN))),
                studyProgramId);

        if (!studentService.existsByStudentNumber(studentNumber)) {
            return studentService.createStudent(createDto).getId();
        }

        Long id = studentService.findStudentByStudentNumber(studentNumber).getId();
        studentService.patchStudentById(id, new StudentPatchDTO(
                createDto.firstName(), createDto.lastName(), createDto.address(),
                createDto.email(), createDto.phoneNumber(), createDto.studentNumber(),
                createDto.salutation(), createDto.academicLevel(), createDto.studyProgramId()));
        return id;
    }

    private Long upsertScientificWork(Row row, Long studentId, Long studyProgramId, Long mainEvalId, Long secondEvalId) {
        LocalDate start = getSafeLocalDate(row.getCell(START_DATE_COLUMN), "StartDate");
        LocalTime pStart = getSafeLocalTime(row.getCell(PRESENTATION_START_COLUMN), "PresStart");
        LocalTime pEnd = getSafeLocalTime(row.getCell(PRESENTATION_END_COLUMN), "PresEnd");

        ScientificWorkCreateDTO dto = new ScientificWorkCreateDTO(
                getSafeLocalDate(row.getCell(COLLOQUIUM_DATE_COLUMN), "ColloqDate").atTime(pStart),
                getSafeString(row.getCell(COLLOQUIUM_LOCATION_COLUMN)),
                Duration.between(pStart, pEnd),
                pStart, pEnd,
                getSafeLocalTime(row.getCell(DISCUSSION_START_COLUMN), "DiscStart"),
                getSafeLocalTime(row.getCell(DISCUSSION_END_COLUMN), "DiscEnd"),
                getSafeString(row.getCell(WORK_TITLE_COLUMN)), start,
                getSafeLocalDate(row.getCell(END_DATE_COLUMN), "EndDate"),
                studentId, studyProgramId, mainEvalId,
                (int) getSafeNumeric(row.getCell(MARK_MAIN_COLLOQ)),
                (int) getSafeNumeric(row.getCell(MARK_MAIN_WORK)),
                secondEvalId,
                (int) getSafeNumeric(row.getCell(MARK_SECOND_COLLOQ)),
                (int) getSafeNumeric(row.getCell(MARK_SECOND_WORK)),
                getSafeString(row.getCell(COMMENT_COLUMN)));

        if (!scientificWorkService.existsByStartDateAndStudent(start, studentId)) {
            ScientificWork created = scientificWorkService.create(dto);
            return created.getId();
        } else {
            Long id = scientificWorkService.findByStartDateAndStudent(start, studentId).getId();
            scientificWorkService.patchScientificWorkById(id, new ScientificWorkPatchDTO(
                    dto.colloquium(), dto.colloquiumLocation(), dto.colloquiumDuration(),
                    dto.presentationStart(), dto.presentationEnd(), dto.discussionStart(), dto.discussionEnd(),
                    dto.title(), dto.startDate(), dto.endDate(), dto.studentId(), dto.studyProgramId(),
                    dto.mainEvaluatorId(), dto.mainEvaluatorColloquiumMark(), dto.mainEvaluatorWorkMark(),
                    dto.secondEvaluatorId(), dto.secondEvaluatorColloquiumMark(), dto.secondEvaluatorWorkMark(),
                    dto.comment()));
            return id;
        }
    }

    private Long getStudyProgram(Row row) {
        String type = getSafeString(row.getCell(STUDYPROGRAM_DEGREETYPE_COLUMN)).toUpperCase();
        String title = getSafeString(row.getCell(STUDYPROGRAM_TITLE));
        return studyProgramService.findByDegreeTypeAndTitle(DegreeType.valueOfLabel(type), title).getId();
    }

    // --- Helper Parsers ---

    private String getSafeString(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return "";
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((long) cell.getNumericCellValue());
        return cell.getStringCellValue().trim();
    }

    private double getSafeNumeric(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return 0.0;
        if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
        try {
            return Double.parseDouble(cell.getStringCellValue().replace(",", "."));
        } catch (Exception e) {
            return 0.0;
        }
    }

    private LocalDate getSafeLocalDate(Cell cell, String logTag) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return LocalDate.now();
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
            return LocalDate.parse(cell.getStringCellValue().trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    private LocalTime getSafeLocalTime(Cell cell, String logTag) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return LocalTime.MIDNIGHT;
        try {
            if (cell.getCellType() == CellType.NUMERIC) return cell.getLocalDateTimeCellValue().toLocalTime();
            String val = cell.getStringCellValue().trim().toUpperCase();
            if (val.contains("AM") || val.contains("PM")) {
                return LocalTime.parse(val, DateTimeFormatter.ofPattern("h:mm[:ss] a", Locale.US));
            }
            return LocalTime.parse(val);
        } catch (Exception e) {
            return LocalTime.MIDNIGHT;
        }
    }

    private boolean isRowEmpty(Row row) {
        Cell titleCell = row.getCell(WORK_TITLE_COLUMN);
        return titleCell == null || titleCell.getCellType() == CellType.BLANK;
    }

    // ######################### Output ################################
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

    public ResponseEntity<String> resetDatabase() {
        studyProgramService.deleteAllStudyPrograms();
        studentService.deleteAllStudents();
        evaluatorService.deleteAllEvaluators();
        eventService.deleteAllEvents();
        scientificWorkService.deleteAllScientificWorks();
        return ResponseEntity.ok().body("Database successfully reset ");
    }
}