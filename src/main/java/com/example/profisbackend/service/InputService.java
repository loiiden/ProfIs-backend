package com.example.profisbackend.service;

import java.io.FileInputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.example.profisbackend.dto.evaluator.EvaluatorCreateDTO;
import com.example.profisbackend.dto.evaluator.EvaluatorPatchDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkCreateDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkPatchDTO;
import com.example.profisbackend.dto.student.StudentCreateDTO;
import com.example.profisbackend.dto.student.StudentPatchDTO;
import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.enums.EvaluatorRole;
import com.example.profisbackend.enums.Salutation;
import com.example.profisbackend.enums.DegreeType;
import com.example.profisbackend.dto.studyprogram.StudyProgramDTO;
import com.example.profisbackend.mapper.StudyProgramMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import static com.example.profisbackend.config.Constants.*;

@RequiredArgsConstructor
@Service
public class InputService {

    private static final Logger log = LoggerFactory.getLogger(InputService.class);

    private final StudyProgramService studyProgramService;
    private final StudentService studentService;
    private final ScientificWorkService scientificWorkService;
    private final EvaluatorService evaluatorService;

    public ResponseEntity<String> copyToDb(String path) {
        try (FileInputStream file = new FileInputStream(path);
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

                upsertScientificWork(row, studentId, studyProgramId, mainEvalId, secondEvalId);
            } catch (Exception e) {
                log.warn("Skipping row {}: {}", i, e.getMessage());
            }
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

    private void upsertScientificWork(Row row, Long studentId, Long studyProgramId, Long mainEvalId, Long secondEvalId) {
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
            scientificWorkService.create(dto);
        } else {
            Long id = scientificWorkService.findByStartDateAndStudent(start, studentId).getId();
            scientificWorkService.patchScientificWorkById(id, new ScientificWorkPatchDTO(
                    dto.colloquium(), dto.colloquiumLocation(), dto.colloquiumDuration(),
                    dto.presentationStart(), dto.presentationEnd(), dto.discussionStart(), dto.discussionEnd(),
                    dto.title(), dto.startDate(), dto.endDate(), dto.studentId(), dto.studyProgramId(),
                    dto.mainEvaluatorId(), dto.mainEvaluatorColloquiumMark(), dto.mainEvaluatorWorkMark(),
                    dto.secondEvaluatorId(), dto.secondEvaluatorColloquiumMark(), dto.secondEvaluatorWorkMark(),
                    dto.comment()));
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
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((long)cell.getNumericCellValue());
        return cell.getStringCellValue().trim();
    }

    private double getSafeNumeric(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return 0.0;
        if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
        try { return Double.parseDouble(cell.getStringCellValue().replace(",", ".")); }
        catch (Exception e) { return 0.0; }
    }

    private LocalDate getSafeLocalDate(Cell cell, String logTag) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return LocalDate.now();
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
            return LocalDate.parse(cell.getStringCellValue().trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (Exception e) { return LocalDate.now(); }
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
        } catch (Exception e) { return LocalTime.MIDNIGHT; }
    }

    private boolean isRowEmpty(Row row) {
        Cell titleCell = row.getCell(WORK_TITLE_COLUMN);
        return titleCell == null || titleCell.getCellType() == CellType.BLANK;
    }
}