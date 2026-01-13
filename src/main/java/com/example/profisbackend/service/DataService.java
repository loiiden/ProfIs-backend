package com.example.profisbackend.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
import com.example.profisbackend.utils.LocalDateUtility;
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
                if (scientificWorkId != null) {
                    eventService.deleteAllEventsByScientificWorkId(scientificWorkId);
                    upsertAllEvents(row, scientificWorkId);
                }
            } catch (Exception e) {
                log.warn("Skipping row {}: {}", i, e.getMessage());
            }
        }
    }


    private void upsertAllEvents(Row row, Long scientificWorkId) {
        upsertEventsByEventTypeForScientificWork(scientificWorkId, EventType.PLANNED, row, EVENT_PLANNED_COLUMN);
        upsertEventsByEventTypeForScientificWork(scientificWorkId, EventType.PROPOSAL, row, EVENT_PROPOSAL_COLUMN);
        upsertEventsByEventTypeForScientificWork(scientificWorkId, EventType.DISCUSSION, row, EVENT_DISCUSSION_COLUMN);
        upsertEventsByEventTypeForScientificWork(scientificWorkId, EventType.FINAL_SUBMISSION, row, EVENT_FINAL_SUBMISSION_COLUMN);
        upsertEventsByEventTypeForScientificWork(scientificWorkId, EventType.REVIEW, row, EVENT_REVIEW_COLUMN);
        upsertEventsByEventTypeForScientificWork(scientificWorkId, EventType.ARCHIVE, row, EVENT_ARCHIVE_COLUMN);
        upsertEventsByEventTypeForScientificWork(scientificWorkId, EventType.ABORT, row, EVENT_ABORT_COLUMN);
    }


    private void upsertEventsByEventTypeForScientificWork (Long scientificWorkId, EventType eventType, Row row, int column) {
        String eventDatesInStringFormat = getSafeString(row.getCell(column));
        List<LocalDate> eventDatesList = LocalDateUtility.parseEventDatesStringToLocalDates((eventDatesInStringFormat));
        for(LocalDate eventDate : eventDatesList){
            EventCreateDTO eventCreateDTO = new EventCreateDTO(
                eventType,
                eventDate,
                scientificWorkId
            );
            eventService.createEvent(eventCreateDTO);
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
        Long studentNumber = getSafeLong(row.getCell(STUDENT_NUMBER_COLUMN));
        if (studentNumber == null) {
            throw new IllegalArgumentException("Student number is mandatory");
        }
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
        String startDateString =  getSafeString(row.getCell(START_DATE_COLUMN));
        LocalDate startDate = null;
        if (!Objects.equals(startDateString, "")) {
            startDate = LocalDate.parse(startDateString);
        }
        LocalDate endDate = null;

        String endDateString =  getSafeString(row.getCell(END_DATE_COLUMN));
        if (!Objects.equals(endDateString, "")) {
            endDate = LocalDate.parse(endDateString);
        }

        LocalTime pStart = getSafeLocalTime(row.getCell(PRESENTATION_START_COLUMN), "PresStart");
        LocalTime pEnd = getSafeLocalTime(row.getCell(PRESENTATION_END_COLUMN), "PresEnd");

        ScientificWorkCreateDTO dto = new ScientificWorkCreateDTO(
                getSafeLocalDateTime(row.getCell(COLLOQUIUM_DATE_COLUMN), "ColloqDate"),
                getSafeString(row.getCell(COLLOQUIUM_LOCATION_COLUMN)),
                getSafeDuration(row.getCell(DURATION_COLUMN), "Duration"),
                pStart,
                pEnd,
                getSafeLocalTime(row.getCell(DISCUSSION_START_COLUMN), "DiscStart"),
                getSafeLocalTime(row.getCell(DISCUSSION_END_COLUMN), "DiscEnd"),
                getSafeString(row.getCell(WORK_TITLE_COLUMN)),
                startDate,
                endDate,
                studentId, studyProgramId, mainEvalId,
                getSafeInteger(row.getCell(MARK_MAIN_WORK)),
                getSafeInteger(row.getCell(MARK_MAIN_COLLOQ)),
                secondEvalId,
                getSafeInteger(row.getCell(MARK_SECOND_WORK)),
                getSafeInteger(row.getCell(MARK_SECOND_COLLOQ)),
                getSafeString(row.getCell(COMMENT_COLUMN)));

        if (!scientificWorkService.existsByStartDateAndStudent(startDate, studentId)) {
            ScientificWork created = scientificWorkService.create(dto);
            return created.getId();
        } else {
            Long id = scientificWorkService.findByStartDateAndStudent(startDate, studentId).getId();
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
        //Why upper case? It causes a bug with no found study program.
        //String type = getSafeString(row.getCell(STUDYPROGRAM_DEGREETYPE_COLUMN)).toUpperCase();
        String type = getSafeString(row.getCell(STUDYPROGRAM_DEGREETYPE_COLUMN));
        String title = getSafeString(row.getCell(STUDYPROGRAM_TITLE));
        return studyProgramService.findByDegreeTypeAndTitle(DegreeType.valueOfLabel(type), title).getId();
    }

    // --- Helper Parsers ---

    private String getSafeString(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return "";
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((long) cell.getNumericCellValue());
        return cell.getStringCellValue().trim();
    }

    private Double getSafeNumeric(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
        try {
            return Double.parseDouble(cell.getStringCellValue().replace(",", "."));
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getSafeInteger(Cell cell) {
        Double d = getSafeNumeric(cell);
        if (d == null) return null;
        return d.intValue();
    }

    private Long getSafeLong(Cell cell) {
        Double d = getSafeNumeric(cell);
        if (d == null) return null;
        return d.longValue();
    }

    private LocalDate getSafeLocalDate(Cell cell, String logTag) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
            return LocalDate.parse(cell.getStringCellValue().trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (Exception e) {
            return null;
        }
    }

    private Duration getSafeDuration(Cell cell, String logTag) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        try{
            return Duration.parse(cell.getStringCellValue().replace(",", "."));
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime getSafeLocalDateTime(Cell cell, String logTag) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        try{
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue();
            }
            return LocalDateTime.parse(cell.getStringCellValue().trim());
        } catch (Exception e) {
            return null;
        }
    }

    private LocalTime getSafeLocalTime(Cell cell, String logTag) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) return cell.getLocalDateTimeCellValue().toLocalTime();
            String val = cell.getStringCellValue().trim().toUpperCase();
            if (val.contains("AM") || val.contains("PM")) {
                return LocalTime.parse(val, DateTimeFormatter.ofPattern("h:mm[:ss] a", Locale.US));
            }
            return LocalTime.parse(val);
        } catch (Exception e) {
            return null;
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
        addCell(header, FIRST_EVALUATOR_SALUTATION, "Erstprüfer Anrede", style);
        addCell(header, FIRST_EVALUATOR_FIRSTNAME, "Erstprüfer Vorname", style);
        addCell(header, FIRST_EVALUATOR_LASTNAME, "Erstprüfer Nachname", style);
        addCell(header, FIRST_EVALUATOR_ACADEMICLEVEL, "Erstprüfer Abschluss", style);
        addCell(header, FIRST_EVALUATOR_ROLE, "Erstprüfer Rolle", style);
        addCell(header, FIRST_EVALUATOR_ADDRESS, "Erstprüfer Adresse", style);
        addCell(header, FIRST_EVALUATOR_EMAIL, "Erstprüfer E-Mail", style);
        addCell(header, FIRST_EVALUATOR_PHONE, "Erstprüfer Telefon", style);

        addCell(header, SECOND_EVALUATOR_SALUTATION, "Zweitprüfer Anrede", style);
        addCell(header, SECOND_EVALUATOR_FIRSTNAME, "Zweitprüfer Vorname", style);
        addCell(header, SECOND_EVALUATOR_LASTNAME, "Zweitprüfer Nachname", style);
        addCell(header, SECOND_EVALUATOR_ACADEMICLEVEL, "Zweitprüfer Abschluss", style);
        addCell(header, SECOND_EVALUATOR_ROLE, "Zweitprüfer Rolle", style);
        addCell(header, SECOND_EVALUATOR_ADDRESS, "Zweitprüfer Adresse", style);
        addCell(header, SECOND_EVALUATOR_EMAIL, "Zweitprüfer E-Mail", style);
        addCell(header, SECOND_EVALUATOR_PHONE, "Zweitprüfer Telefon", style);

        addCell(header, STUDENT_FIRSTNAME_COLUMN, "Vorname Stud.", style);
        addCell(header, STUDENT_LASTNAME_COLUMN, "Nachname Stud.", style);
        addCell(header, STUDENT_ACADEMICLEVEL_COLUMN, "Abschluss Stud.", style);
        addCell(header, STUDENT_SALUTATION_COLUMN, "Anrede", style);
        addCell(header, STUDENT_PHONENUMBER_COLUMN, "Telefonnummer", style);
        addCell(header, STUDENT_MAIL_COLUMN, "E-Mail Stud.", style);
        addCell(header, STUDENT_NUMBER_COLUMN, "Matrikelnummer", style);
        addCell(header, STUDENT_ADDRESS_COLUMN, "Adresse Stud.", style);

        addCell(header, STUDYPROGRAM_DEGREETYPE_COLUMN, "Studiengang Level", style);
        addCell(header, STUDYPROGRAM_TITLE, "Studiengang Name", style);

        addCell(header, WORK_TITLE_COLUMN, "Titel der Arbeit", style);
        addCell(header, START_DATE_COLUMN, "Startdatum", style);
        addCell(header, END_DATE_COLUMN, "Abgabedatum", style);
        addCell(header, COLLOQUIUM_DATE_COLUMN, "Kolloquium Datum", style);
        addCell(header, DURATION_COLUMN, "Dauer", style);
        addCell(header, COLLOQUIUM_LOCATION_COLUMN, "Kolloquium Ort", style);
        addCell(header, PRESENTATION_START_COLUMN, "Präsentat. Start", style);
        addCell(header, PRESENTATION_END_COLUMN, "Präsentat. Ende", style);
        addCell(header, DISCUSSION_START_COLUMN, "Diskussions Start", style);
        addCell(header, DISCUSSION_END_COLUMN, "Diskussions Ende", style);

        addCell(header, MARK_MAIN_WORK, "Erstprüfer Note Arbeit", style);
        addCell(header, MARK_MAIN_COLLOQ, "Erstprüfer Note Kolloq.", style);
        addCell(header, MARK_SECOND_WORK, "Zweitprüfer Note Arbeit", style);
        addCell(header, MARK_SECOND_COLLOQ, "Zweitprüfer Note Kolloq.", style);

        addCell(header, SWS_COLUMN, "Deputat (SWS)", style);
        addCell(header, COMMENT_COLUMN, "Kommentar", style);
        addCell(header, EVENT_PLANNED_COLUMN, "EventPlanned", style);
        addCell(header, EVENT_PROPOSAL_COLUMN, "EventProposal", style);
        addCell(header, EVENT_DISCUSSION_COLUMN, "EventDiscussion", style);
        addCell(header, EVENT_FINAL_SUBMISSION_COLUMN, "EventFinalSubmission", style);
        addCell(header, EVENT_REVIEW_COLUMN, "EventReview", style);
        addCell(header, EVENT_ARCHIVE_COLUMN, "EventArchive", style);
        addCell(header, EVENT_ABORT_COLUMN, "EventAbort", style);
    }



    private void createCellInteger(Row row, int colIndex, Integer value){
        Cell cell = row.createCell(colIndex);
        if (value != null){
            cell.setCellValue(value);
        }
    }

    private void createCellString(Row row, int colIndex, String value){
        Cell cell = row.createCell(colIndex);
        if (value != null){
            cell.setCellValue(value);
        }
    }
    private void createCellLong(Row row, int colIndex, Long value){
        Cell cell = row.createCell(colIndex);
        if (value != null){
            cell.setCellValue(value);
        }
    }

    private void createCellDouble(Row row, int colIndex, Double value){
        Cell cell = row.createCell(colIndex);
        if (value != null){
            cell.setCellValue(value);
        }
    }

    private void fillWorkData(Sheet sheet) {
        List<ScientificWork> works = scientificWorkService.findAll();
        int rowIdx = 1;

        for (ScientificWork work : works) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(eventService.getCurrentStatusForScientificWorkByScientificWorkId(work.getId()).getEventType().toString());

            if (work.getMainEvaluator() != null) {
                Evaluator me = work.getMainEvaluator();
                createCellString(row, FIRST_EVALUATOR_SALUTATION, me.getSalutation() != null ? me.getSalutation().getLabel() : null);
                createCellString(row, FIRST_EVALUATOR_FIRSTNAME, me.getFirstName());
                createCellString(row, FIRST_EVALUATOR_LASTNAME, me.getLastName());
                createCellString(row, FIRST_EVALUATOR_ACADEMICLEVEL, me.getAcademicLevel() != null ? me.getAcademicLevel().getLabel() : null);
                //TODO: CONTROL THAT IR WORKS
                createCellString(row, FIRST_EVALUATOR_ROLE, me.getRole() != null ? me.getRole().getLabel() : null);
                createCellString(row, FIRST_EVALUATOR_ADDRESS, me.getAddress());
                createCellString(row, FIRST_EVALUATOR_EMAIL, me.getEmail());
                createCellString(row, FIRST_EVALUATOR_PHONE, me.getPhoneNumber());
            }

            if (work.getSecondEvaluator() != null) {
                Evaluator se = work.getSecondEvaluator();

                createCellString(row, SECOND_EVALUATOR_SALUTATION, se.getSalutation() != null ? se.getSalutation().getLabel() : null);
                createCellString(row, SECOND_EVALUATOR_FIRSTNAME, se.getFirstName());
                createCellString(row, SECOND_EVALUATOR_LASTNAME, se.getLastName());
                createCellString(row, SECOND_EVALUATOR_ACADEMICLEVEL, se.getAcademicLevel() != null ? se.getAcademicLevel().getLabel() : null);
                createCellString(row, SECOND_EVALUATOR_ROLE, se.getRole() != null ? se.getRole().getLabel() : null);
                createCellString(row, SECOND_EVALUATOR_ADDRESS, se.getAddress());
                createCellString(row, SECOND_EVALUATOR_EMAIL, se.getEmail());
                createCellString(row, SECOND_EVALUATOR_PHONE, se.getPhoneNumber());
            }

            if (work.getStudent() != null) {
                Student s = work.getStudent();

                createCellString(row, STUDENT_FIRSTNAME_COLUMN, s.getFirstName());
                createCellString(row, STUDENT_LASTNAME_COLUMN, s.getLastName());
                createCellString(row, STUDENT_ACADEMICLEVEL_COLUMN, s.getAcademicLevel() != null ? s.getAcademicLevel().getLabel() : null);
                createCellString(row, STUDENT_SALUTATION_COLUMN, s.getSalutation() != null ? s.getSalutation().getLabel() : null);
                createCellString(row, STUDENT_PHONENUMBER_COLUMN, s.getPhoneNumber());
                createCellString(row, STUDENT_MAIL_COLUMN, s.getEmail());
                createCellLong(row, STUDENT_NUMBER_COLUMN, s.getStudentNumber());
                createCellString(row, STUDENT_ADDRESS_COLUMN, s.getAddress());
            }

            if (work.getStudyProgram() != null) {
                createCellString(row, STUDYPROGRAM_DEGREETYPE_COLUMN, work.getStudyProgram().getDegreeType() != null ? work.getStudyProgram().getDegreeType().getLabel() : null);
                createCellString(row, STUDYPROGRAM_TITLE, work.getStudyProgram().getTitle());
            }

            createCellString(row, WORK_TITLE_COLUMN, work.getTitle());
            createCellString(row, START_DATE_COLUMN, work.getStartDate() != null ? work.getStartDate().toString() : null);
            createCellString(row, END_DATE_COLUMN, work.getEndDate() != null ? work.getEndDate().toString() : null);
            createCellString(row, COLLOQUIUM_DATE_COLUMN, work.getColloquium() != null ? work.getColloquium().toString() : null);
            createCellString(row, DURATION_COLUMN, work.getColloquiumDuration() != null ? work.getColloquiumDuration().toString() : null);
            createCellString(row, COLLOQUIUM_LOCATION_COLUMN, work.getColloquiumLocation());
            createCellString(row, PRESENTATION_START_COLUMN, work.getPresentationStart() != null ? work.getPresentationStart().toString() : null);
            createCellString(row, PRESENTATION_END_COLUMN, work.getPresentationEnd() != null ? work.getPresentationEnd().toString() : null );
            createCellString(row, DISCUSSION_START_COLUMN, work.getDiscussionStart() != null ? work.getDiscussionStart().toString() : null);
            createCellString(row, DISCUSSION_END_COLUMN, work.getDiscussionEnd() != null ? work.getDiscussionEnd().toString() : null);


            // This prevents using nulls as a value and just leave it blank
            createCellInteger(row, MARK_MAIN_WORK, work.getMainEvaluatorWorkMark());
            createCellInteger(row, MARK_MAIN_COLLOQ, work.getMainEvaluatorColloquiumMark());
            createCellInteger(row, MARK_SECOND_WORK, work.getSecondEvaluatorWorkMark());
            createCellInteger(row, MARK_SECOND_COLLOQ, work.getSecondEvaluatorColloquiumMark());

            // sws in study program must be initialized and not null
            createCellDouble(row, SWS_COLUMN, work.getStudyProgram() != null ? work.getStudyProgram().getSws() : 0.0);
            createCellString(row, COMMENT_COLUMN, work.getComment());
            row.createCell(EVENT_PLANNED_COLUMN).setCellValue(getAndMapLocalDatesToStringByEventTypeAndScientificWorkId(EventType.PLANNED, work.getId()));
            row.createCell(EVENT_PROPOSAL_COLUMN).setCellValue(getAndMapLocalDatesToStringByEventTypeAndScientificWorkId(EventType.PROPOSAL, work.getId()));
            row.createCell(EVENT_DISCUSSION_COLUMN).setCellValue(getAndMapLocalDatesToStringByEventTypeAndScientificWorkId(EventType.DISCUSSION, work.getId()));
            row.createCell(EVENT_FINAL_SUBMISSION_COLUMN).setCellValue(getAndMapLocalDatesToStringByEventTypeAndScientificWorkId(EventType.FINAL_SUBMISSION, work.getId()));
            row.createCell(EVENT_REVIEW_COLUMN).setCellValue(getAndMapLocalDatesToStringByEventTypeAndScientificWorkId(EventType.REVIEW, work.getId()));
            row.createCell(EVENT_ARCHIVE_COLUMN).setCellValue(getAndMapLocalDatesToStringByEventTypeAndScientificWorkId(EventType.ARCHIVE, work.getId()));
            row.createCell(EVENT_ABORT_COLUMN).setCellValue(getAndMapLocalDatesToStringByEventTypeAndScientificWorkId(EventType.ABORT, work.getId()));
        }
    }


    private String getAndMapLocalDatesToStringByEventTypeAndScientificWorkId(EventType eventType, Long scientificWorkId) {
        String res = LocalDateUtility.parseEventDatesToString(
                //get Events
                eventService.findEventsByEventTypeAndScientificWorkId(eventType, scientificWorkId)
                        .stream().map(
                                //get localDate of every event
                                Event::getEventDate
                        ).toList()
                //map it to string. Null will be mapped to noting. "";
        );
        System.out.println(res);
        return res;
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