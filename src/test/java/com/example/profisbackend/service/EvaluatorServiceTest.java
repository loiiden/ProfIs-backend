package com.example.profisbackend.service;

import com.example.profisbackend.dto.evaluator.EvaluatorCreateDTO;
import com.example.profisbackend.dto.evaluator.EvaluatorResponseDTO;
import com.example.profisbackend.entities.Evaluator;
import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.enums.EvaluatorRole;
import com.example.profisbackend.enums.Salutation;
import com.example.profisbackend.repository.EvaluatorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class EvaluatorServiceTest {
    @Autowired
    EvaluatorRepository evaluatorRepository;
    @Autowired
    EvaluatorService evaluatorService;

    @Test
    public void makeEvaluatorMainUser() {
        EvaluatorCreateDTO createEvaluatorDto1 = new EvaluatorCreateDTO(
                "Maksim",
                "Doe",
                "some adress",
                "@example.com",
                "123",
                AcademicLevel.MASTER,
                EvaluatorRole.PROFESSOR,
                Salutation.HERR);
        Evaluator created = evaluatorService.createEvaluator(createEvaluatorDto1);
        evaluatorService.makeEvaluatorMainUserById(created.getId());
        assertThat(created.getMainUserOfSystem());
        assertThat(evaluatorService.findMainUser().isPresent());
        assertThat(evaluatorService.findMainUser().get().getId()).isEqualTo(created.getId());

    }
}
