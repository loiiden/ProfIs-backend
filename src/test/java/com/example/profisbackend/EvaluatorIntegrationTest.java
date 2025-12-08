package com.example.profisbackend;

import com.example.profisbackend.dto.EvaluatorDto;
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
    private EvaluatorDto createSampleEvaluator(String firstName, String phone) {
        EvaluatorDto create = new EvaluatorDto(null, firstName, "Doe", firstName.toLowerCase() + "@example.com", phone, AcademicLevel.MASTER_UNI, EvaluatorRole.PROFESSOR);
        ResponseEntity<EvaluatorDto> createResp = restTemplate.postForEntity("/evaluator", create, EvaluatorDto.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        EvaluatorDto created = createResp.getBody();
        assertThat(created).isNotNull();
        assertThat(created.id()).isNotNull();
        return created;
    }

    // --- Small focused tests ---
    @Test
    public void createEvaluator_returnsCreatedWithId() {
        EvaluatorDto created = createSampleEvaluator("John", "123");
        assertThat(created.firstName()).isEqualTo("John");
    }

    @Test
    public void getAll_includesCreatedEvaluator() {
        EvaluatorDto created = createSampleEvaluator("Alice", "321");
        ResponseEntity<List<EvaluatorDto>> allResp = restTemplate.exchange(
                "/evaluator",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){}
        );
        assertThat(allResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<EvaluatorDto> list = allResp.getBody();
        assertThat(list).isNotEmpty();
        boolean found = list.stream().anyMatch(e -> created.id().equals(e.id()));
        assertThat(found).isTrue();
    }

    @Test
    public void patchEvaluator_updatesFields() {
        EvaluatorDto created = createSampleEvaluator("Bob", "555");
        Long id = created.id();

        EvaluatorDto patch = new EvaluatorDto(null, "Bobby", null, null, "999", null, null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EvaluatorDto> patchReq = new HttpEntity<>(patch, headers);
        ResponseEntity<EvaluatorDto> patchResp = restTemplate.exchange("/evaluator/" + id, HttpMethod.PATCH, patchReq, EvaluatorDto.class);
        assertThat(patchResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        EvaluatorDto patched = patchResp.getBody();
        assertThat(patched).isNotNull();
        assertThat(patched.firstName()).isEqualTo("Bobby");
        assertThat(patched.phoneNumber()).isEqualTo("999");
    }

    @Test
    public void deleteEvaluator_removesEntity() {
        EvaluatorDto created = createSampleEvaluator("Carl", "777");
        Long id = created.id();

        ResponseEntity<Void> delResp = restTemplate.exchange("/evaluator/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(delResp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<EvaluatorDto> getResp = restTemplate.getForEntity("/evaluator/" + id, EvaluatorDto.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void patchNotFoundReturns404() {
        EvaluatorDto patch = new EvaluatorDto(null, "Nobody", null, null, null, null, null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EvaluatorDto> patchReq = new HttpEntity<>(patch, headers);
        ResponseEntity<EvaluatorDto> patchResp = restTemplate.exchange("/evaluator/9999999", HttpMethod.PATCH, patchReq, EvaluatorDto.class);
        assertThat(patchResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteNotFoundReturns404() {
        ResponseEntity<Void> delResp = restTemplate.exchange("/evaluator/9999999", HttpMethod.DELETE, null, Void.class);
        assertThat(delResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
