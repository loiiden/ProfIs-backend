package com.example.profisbackend;

import com.example.profisbackend.dto.evaluator.EvaluatorCreateDTO;
import com.example.profisbackend.dto.evaluator.EvaluatorResponseDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkCreateDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkResponseDTO;
import com.example.profisbackend.dto.studyprogram.StudyProgramDTO;
import com.example.profisbackend.entities.StudyProgram;
import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.enums.DegreeType;
import com.example.profisbackend.enums.EvaluatorRole;
import com.example.profisbackend.enums.Salutation;
import com.example.profisbackend.service.EvaluatorService;
import com.example.profisbackend.service.StatisticsService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SwsTest {
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private EvaluatorService evaluatorService;

    @BeforeEach
    public void cleanup() {
        //if needed. It should be deleted after all tests automaticaly.
    }


    @Test
    @Transactional
    public void testSwsWithNoWorks() {
        EvaluatorCreateDTO createEvaluatorDTO1 = new EvaluatorCreateDTO(
                "Maksim",
                "Efremov",
                "Adress",
                "maksim@gmail",
                "123",
                AcademicLevel.BACHELOR,
                EvaluatorRole.PROFESSOR,
                Salutation.HERR
        );

        ResponseEntity<EvaluatorResponseDTO> createResp = restTemplate.postForEntity("/api/evaluator", createEvaluatorDTO1, EvaluatorResponseDTO.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResp.getBody()).isNotNull();
        Long evaluatorId = createResp.getBody().id();

        Double SWS = statisticsService.getSwsInCurrentSemesterForEvaluatorByEvaluatorId(evaluatorId);
        assertThat(SWS).isEqualTo(0);
    }


    @Test
    @Transactional
    public void testSwsWithTwoWorksAndOneEvaluator() {
        EvaluatorCreateDTO createEvaluatorDTO1 = new EvaluatorCreateDTO(
                "Maksim",
                "Efremov",
                "Adress",
                "maksim@gmail",
                "123",
                AcademicLevel.BACHELOR,
                EvaluatorRole.PROFESSOR,
                Salutation.HERR
        );

        ResponseEntity<EvaluatorResponseDTO> createResp = restTemplate.postForEntity("/api/evaluator", createEvaluatorDTO1, EvaluatorResponseDTO.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResp.getBody()).isNotNull();
        Long evaluatorId = createResp.getBody().id();
        StudyProgramDTO studyProgramDTO = new StudyProgramDTO(
                DegreeType.B_SC,
                "Computer Science",
                0.3
        );

        ResponseEntity<StudyProgram> createStudyProgramResp = restTemplate.postForEntity("/api/study-program", studyProgramDTO, StudyProgram.class);
        assertThat(createStudyProgramResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createStudyProgramResp.getBody()).isNotNull();
        Long studyProgramId = createStudyProgramResp.getBody().getId();
        ScientificWorkCreateDTO scientificWorkCreateDTO1 = new ScientificWorkCreateDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Title 1",
                LocalDate.of(2026, 1, 3),
                LocalDate.of(2026, 1, 4),
                null,
                studyProgramId,
                evaluatorId,
                null,
                null,
                null,
                null,
                null,
                null
        );
        ScientificWorkCreateDTO scientificWorkCreateDTO2 = new ScientificWorkCreateDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Title 2",
                LocalDate.of(2026, 1, 6),
                LocalDate.of(2026, 1, 7),
                null,
                studyProgramId,
                evaluatorId,
                null,
                null,
                null,
                null,
                null,
                null
        );

        ResponseEntity<ScientificWorkResponseDTO> createScientificWorkResp1 = restTemplate.postForEntity("/api/scientific-work", scientificWorkCreateDTO1, ScientificWorkResponseDTO.class);
        ResponseEntity<ScientificWorkResponseDTO> createScientificWorkResp2 = restTemplate.postForEntity("/api/scientific-work", scientificWorkCreateDTO1, ScientificWorkResponseDTO.class);
        System.out.println(evaluatorService.findById(evaluatorId).getScientificWorksAsMainEvaluator());
        Double SWS = statisticsService.getSwsInCurrentSemesterForEvaluatorByEvaluatorId(evaluatorId);
        assertThat(SWS).isEqualTo(0.6);
    }
    @Test
    @Transactional
    public void testSwsWithTwoStudyPrograms() {
        EvaluatorCreateDTO createEvaluatorDTO1 = new EvaluatorCreateDTO(
                "Maksim",
                "Efremov",
                "Adress",
                "maksim@gmail",
                "123",
                AcademicLevel.BACHELOR,
                EvaluatorRole.PROFESSOR,
                Salutation.HERR
        );

        ResponseEntity<EvaluatorResponseDTO> createResp = restTemplate.postForEntity("/api/evaluator", createEvaluatorDTO1, EvaluatorResponseDTO.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResp.getBody()).isNotNull();
        Long evaluatorId = createResp.getBody().id();


        StudyProgramDTO studyProgramDTO = new StudyProgramDTO(
                DegreeType.B_SC,
                "Computer Science",
                0.3
        );

        ResponseEntity<StudyProgram> createStudyProgramResp = restTemplate.postForEntity("/api/study-program", studyProgramDTO, StudyProgram.class);
        assertThat(createStudyProgramResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createStudyProgramResp.getBody()).isNotNull();
        Long studyProgramId = createStudyProgramResp.getBody().getId();

        StudyProgramDTO studyProgramDTO2 = new StudyProgramDTO(
                DegreeType.B_SC,
                "Computer Science",
                0.5
        );

        ResponseEntity<StudyProgram> createStudyProgramResp2 = restTemplate.postForEntity("/api/study-program", studyProgramDTO2, StudyProgram.class);
        assertThat(createStudyProgramResp2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createStudyProgramResp2.getBody()).isNotNull();
        Long studyProgramId2 = createStudyProgramResp2.getBody().getId();


        ScientificWorkCreateDTO scientificWorkCreateDTO1 = new ScientificWorkCreateDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Title 1",
                LocalDate.of(2026, 1, 3),
                LocalDate.of(2026, 1, 4),
                null,
                studyProgramId,
                evaluatorId,
                null,
                null,
                null,
                null,
                null,
                null
        );
        ScientificWorkCreateDTO scientificWorkCreateDTO2 = new ScientificWorkCreateDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Title 2",
                LocalDate.of(2026, 1, 6),
                LocalDate.of(2026, 1, 7),
                null,
                studyProgramId2,
                evaluatorId,
                null,
                null,
                null,
                null,
                null,
                null
        );

        ResponseEntity<ScientificWorkResponseDTO> createScientificWorkResp1 = restTemplate.postForEntity("/api/scientific-work", scientificWorkCreateDTO1, ScientificWorkResponseDTO.class);
        ResponseEntity<ScientificWorkResponseDTO> createScientificWorkResp2 = restTemplate.postForEntity("/api/scientific-work", scientificWorkCreateDTO2, ScientificWorkResponseDTO.class);
        System.out.println(evaluatorService.findById(evaluatorId).getScientificWorksAsMainEvaluator());
        Double SWS = statisticsService.getSwsInCurrentSemesterForEvaluatorByEvaluatorId(evaluatorId);
        assertThat(SWS).isEqualTo(0.3 + 0.5);
    }

    @Test
    @Transactional
    public void testSwsWithTwoEvaluators() {
        EvaluatorCreateDTO createEvaluatorDTO1 = new EvaluatorCreateDTO(
                "Maksim",
                "Efremov",
                "Adress",
                "maksim@gmail",
                "123",
                AcademicLevel.BACHELOR,
                EvaluatorRole.PROFESSOR,
                Salutation.HERR
        );

        ResponseEntity<EvaluatorResponseDTO> createResp = restTemplate.postForEntity("/api/evaluator", createEvaluatorDTO1, EvaluatorResponseDTO.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResp.getBody()).isNotNull();
        Long evaluatorId = createResp.getBody().id();

        EvaluatorCreateDTO createEvaluatorDTO2 = new EvaluatorCreateDTO(
                "FirstName",
                "LastName",
                "Adress",
                "email@gmail",
                "1234",
                AcademicLevel.BACHELOR,
                EvaluatorRole.PROFESSOR,
                Salutation.HERR
        );

        ResponseEntity<EvaluatorResponseDTO> createResp2 = restTemplate.postForEntity("/api/evaluator", createEvaluatorDTO2, EvaluatorResponseDTO.class);
        assertThat(createResp2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResp2.getBody()).isNotNull();
        Long evaluatorId2 = createResp2.getBody().id();


        StudyProgramDTO studyProgramDTO = new StudyProgramDTO(
                DegreeType.B_SC,
                "Computer Science",
                0.3
        );

        ResponseEntity<StudyProgram> createStudyProgramResp = restTemplate.postForEntity("/api/study-program", studyProgramDTO, StudyProgram.class);
        assertThat(createStudyProgramResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createStudyProgramResp.getBody()).isNotNull();
        Long studyProgramId = createStudyProgramResp.getBody().getId();


        ScientificWorkCreateDTO scientificWorkCreateDTO1 = new ScientificWorkCreateDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Title 1",
                LocalDate.of(2026, 1, 3),
                LocalDate.of(2026, 1, 4),
                null,
                studyProgramId,
                evaluatorId,
                null,
                null,
                null,
                null,
                null,
                null
        );
        ScientificWorkCreateDTO scientificWorkCreateDTO2 = new ScientificWorkCreateDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Title 2",
                LocalDate.of(2026, 1, 6),
                LocalDate.of(2026, 1, 7),
                null,
                studyProgramId,
                evaluatorId2,
                null,
                null,
                null,
                null,
                null,
                null
        );

        ResponseEntity<ScientificWorkResponseDTO> createScientificWorkResp1 = restTemplate.postForEntity("/api/scientific-work", scientificWorkCreateDTO1, ScientificWorkResponseDTO.class);
        ResponseEntity<ScientificWorkResponseDTO> createScientificWorkResp2 = restTemplate.postForEntity("/api/scientific-work", scientificWorkCreateDTO2, ScientificWorkResponseDTO.class);
        System.out.println(evaluatorService.findById(evaluatorId).getScientificWorksAsMainEvaluator());
        Double SWS1 = statisticsService.getSwsInCurrentSemesterForEvaluatorByEvaluatorId(evaluatorId);
        Double SWS2 = statisticsService.getSwsInCurrentSemesterForEvaluatorByEvaluatorId(evaluatorId2);
        assertThat(SWS1).isEqualTo(0.3);
        assertThat(SWS2).isEqualTo(0.3);
    }

    @Test
    @Transactional
    public void testSwsWithTwoEvaluatorsAndOnlyOneMainUser() {
        EvaluatorCreateDTO mainUserEvaluatorCreateDTO = new EvaluatorCreateDTO(
                "Maksim",
                "Efremov",
                "Adress",
                "maksim@gmail",
                "123",
                AcademicLevel.BACHELOR,
                EvaluatorRole.PROFESSOR,
                Salutation.HERR
        );

        ResponseEntity<EvaluatorResponseDTO> createResp = restTemplate.postForEntity("/api/evaluator", mainUserEvaluatorCreateDTO, EvaluatorResponseDTO.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResp.getBody()).isNotNull();
        Long mainUserEvaluatorId = createResp.getBody().id();
        ResponseEntity<Void> makeEvaluatorMainUserResp = restTemplate.exchange("/api/evaluator/main-user/" + mainUserEvaluatorId, HttpMethod.POST, null, Void.class);
        ResponseEntity<EvaluatorResponseDTO> getMainUserResp = restTemplate.getForEntity("/api/evaluator/main-user", EvaluatorResponseDTO.class);
        assertThat(getMainUserResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getMainUserResp.getBody() != null);
        assertThat(getMainUserResp.getBody().id().equals(createResp.getBody().id())).as("The main user is chosen right");

        EvaluatorCreateDTO normalEvaluatorCreateDTO = new EvaluatorCreateDTO(
                "FirstName",
                "LastName",
                "Adress",
                "email@gmail",
                "1234",
                AcademicLevel.BACHELOR,
                EvaluatorRole.PROFESSOR,
                Salutation.HERR
        );

        ResponseEntity<EvaluatorResponseDTO> createResp2 = restTemplate.postForEntity("/api/evaluator", normalEvaluatorCreateDTO, EvaluatorResponseDTO.class);
        assertThat(createResp2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResp2.getBody()).isNotNull();
        Long normalEvaluatorId = createResp2.getBody().id();


        StudyProgramDTO studyProgramDTO = new StudyProgramDTO(
                DegreeType.B_SC,
                "Computer Science",
                0.3
        );

        ResponseEntity<StudyProgram> createStudyProgramResp = restTemplate.postForEntity("/api/study-program", studyProgramDTO, StudyProgram.class);
        assertThat(createStudyProgramResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createStudyProgramResp.getBody()).isNotNull();
        Long studyProgramId = createStudyProgramResp.getBody().getId();


        ScientificWorkCreateDTO scientificWorkCreateDTO1 = new ScientificWorkCreateDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Title 1",
                LocalDate.of(2026, 1, 3),
                LocalDate.of(2026, 1, 4),
                null,
                studyProgramId,
                mainUserEvaluatorId,
                null,
                null,
                null,
                null,
                null,
                null
        );
        ScientificWorkCreateDTO scientificWorkCreateDTO2 = new ScientificWorkCreateDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Title 2",
                LocalDate.of(2026, 1, 6),
                LocalDate.of(2026, 1, 7),
                null,
                studyProgramId,
                mainUserEvaluatorId,
                null,
                null,
                null,
                null,
                null,
                null
        );

        ResponseEntity<ScientificWorkResponseDTO> createScientificWorkResp1 = restTemplate.postForEntity("/api/scientific-work", scientificWorkCreateDTO1, ScientificWorkResponseDTO.class);
        ResponseEntity<ScientificWorkResponseDTO> createScientificWorkResp2 = restTemplate.postForEntity("/api/scientific-work", scientificWorkCreateDTO2, ScientificWorkResponseDTO.class);


        ResponseEntity<Double> mainUserSWSResp = restTemplate.exchange("/api/sws/main-user/current", HttpMethod.GET, null, Double.class);
        assertThat(mainUserSWSResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(mainUserSWSResp.getBody()).isNotNull();
        Double mainUserSWS = mainUserSWSResp.getBody();

        ResponseEntity<Double> normalEvaluatorSWSResp = restTemplate.exchange("/api/sws/" + normalEvaluatorId + "/current", HttpMethod.GET, null, Double.class);
        assertThat(normalEvaluatorSWSResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(normalEvaluatorSWSResp.getBody()).isNotNull();
        Double normalEvaluatorSWS = normalEvaluatorSWSResp.getBody();
        assertThat(mainUserSWS).isEqualTo(0.6);
        assertThat(normalEvaluatorSWS).isEqualTo(0.0);

        //we switch main user
        ResponseEntity<Void> makeEvaluatorMainUserResp2 = restTemplate.exchange("/api/evaluator/main-user/" + normalEvaluatorId, HttpMethod.POST, null, Void.class);
        assertThat(statisticsService.getSwsInCurrentSemesterForMainUser()).isEqualTo(0.0);
    }
}
