package com.example.profisbackend;

import com.example.profisbackend.dto.evaluator.EvaluatorCreateDTO;
import com.example.profisbackend.dto.evaluator.EvaluatorResponseDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkCreateDTO;
import com.example.profisbackend.dto.studyprogram.StudyProgramDTO;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.enums.DegreeType;
import com.example.profisbackend.enums.EvaluatorRole;
import com.example.profisbackend.enums.Salutation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SwsTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @BeforeEach
    public void cleanup() {
        //if needed. It should be deleted after all tests automaticaly.
    }
    @Test
    public void testSws() {
        EvaluatorCreateDTO createDTO = new EvaluatorCreateDTO(
                "Maksim",
                "Efremov",
                "Adress",
                "maksim@gmail",
                "123",
                AcademicLevel.BACHELOR,
                EvaluatorRole.PROFESSOR,
                Salutation.HERR
        );
        ResponseEntity<EvaluatorResponseDTO> createResp = restTemplate.postForEntity("/api/evaluator", createDTO, EvaluatorResponseDTO.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResp.getBody()).isNotNull();
        Long evaluatorId = createResp.getBody().id();
        StudyProgramDTO studyProgramDTO = new StudyProgramDTO(
                DegreeType.B_SC,
                "Computer Science",
                0.3
        );
        /*ScientificWorkCreateDTO scientificWorkCreateDTO = new ScientificWorkCreateDTO();*/
    }
}
