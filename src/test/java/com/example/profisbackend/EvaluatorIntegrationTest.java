package com.example.profisbackend;

import com.example.profisbackend.dto.evaluator.EvaluatorCreateDTO;
import com.example.profisbackend.dto.evaluator.EvaluatorPatchDTO;
import com.example.profisbackend.dto.evaluator.EvaluatorResponseDTO;
import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.enums.EvaluatorRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EvaluatorIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void cleanup() {
        // no-op: tests run against an in-memory or file DB depending on application.yml
    }

    // --- Helper ---
    private EvaluatorResponseDTO createSampleEvaluator(String firstName, String phone) {
        EvaluatorCreateDTO create = new EvaluatorCreateDTO(firstName, "Doe", firstName.toLowerCase() + "@example.com", phone, null, AcademicLevel.MASTER, EvaluatorRole.PROFESSOR);
        ResponseEntity<EvaluatorResponseDTO> createResp = restTemplate.postForEntity("/api/evaluator", create, EvaluatorResponseDTO.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        EvaluatorResponseDTO created = createResp.getBody();
        assertThat(created).isNotNull();
        assertThat(created.id()).isNotNull();
        return created;
    }

    // --- Small focused tests ---
    @Test
    public void createEvaluator_returnsCreatedWithId() {
        EvaluatorResponseDTO created = createSampleEvaluator("John", "123");
        assertThat(created.firstName()).isEqualTo("John");
    }

    @Test
    public void getAll_includesCreatedEvaluator() {
        EvaluatorResponseDTO created = createSampleEvaluator("Alice", "321");
        ResponseEntity<List<EvaluatorResponseDTO>> allResp = restTemplate.exchange(
                "/api/evaluator",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){}
        );
        assertThat(allResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<EvaluatorResponseDTO> list = allResp.getBody();
        assertThat(list).isNotEmpty();
        boolean found = list.stream().anyMatch(e -> created.id().equals(e.id()));
        assertThat(found).isTrue();
    }

    @Test
    public void patchEvaluator_updatesFields() {
        EvaluatorResponseDTO created = createSampleEvaluator("Bob", "555");
        Long id = created.id();

        EvaluatorPatchDTO patch = new EvaluatorPatchDTO("Bobby", null, null, null, "999", null, null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EvaluatorPatchDTO> patchReq = new HttpEntity<>(patch, headers);
        ResponseEntity<EvaluatorResponseDTO> patchResp = restTemplate.exchange("/api/evaluator/" + id, HttpMethod.PATCH, patchReq, EvaluatorResponseDTO.class);
        assertThat(patchResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        EvaluatorResponseDTO patched = patchResp.getBody();
        assertThat(patched).isNotNull();
        assertThat(patched.firstName()).isEqualTo("Bobby");
        assertThat(patched.phoneNumber()).isEqualTo("999");
    }

    @Test
    public void deleteEvaluator_removesEntity() {
        EvaluatorResponseDTO created = createSampleEvaluator("Carl", "777");
        Long id = created.id();

        ResponseEntity<Void> delResp = restTemplate.exchange("/api/evaluator/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(delResp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<EvaluatorResponseDTO> getResp = restTemplate.getForEntity("/api/evaluator/" + id, EvaluatorResponseDTO.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void patchNotFoundReturns404() {
        EvaluatorPatchDTO patch = new EvaluatorPatchDTO("Nobody", null, null, null, null, null, null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EvaluatorPatchDTO> patchReq = new HttpEntity<>(patch, headers);
        ResponseEntity<EvaluatorResponseDTO> patchResp = restTemplate.exchange("/api/evaluator/9999999", HttpMethod.PATCH, patchReq, EvaluatorResponseDTO.class);
        assertThat(patchResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteNotFoundReturns404() {
        ResponseEntity<Void> delResp = restTemplate.exchange("/api/evaluator/9999999", HttpMethod.DELETE, null, Void.class);
        assertThat(delResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
