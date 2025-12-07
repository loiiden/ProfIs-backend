package com.example.profisbackend;

import com.example.profisbackend.dto.ReferentDto;
import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.enums.EvaluatorRole;
import com.example.profisbackend.model.Referent;
import com.example.profisbackend.service.ReferentService;
import com.example.profisbackend.controller.ReferentController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Lightweight controller test for the Referent REST API.
 *
 * This test uses @WebMvcTest to instantiate only the MVC layer (controller, filters, converters)
 * and mocks the `ReferentService` so we don't need a database. It verifies:
 * - POST /referent returns 201 Created with Location header and JSON body
 * - GET /referent/{id} returns 200 OK with the expected JSON body
 */
@WebMvcTest(ReferentController.class)
public class ReferentApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReferentService referentService;

    @Test
    void postReferent_createsAndReturnsDto() throws Exception {
        // Arrange: prepare the Referent the service should return after save
        Referent saved = new Referent();
        saved.setId(1L);
        saved.setFirstName("Alice");
        saved.setLastName("Smith");
        saved.setEmail("alice@example.com");
        saved.setPhoneNumber("555-1234");
        saved.setAcademicLevel(AcademicLevel.MASTER_UNI);
        saved.setRole(EvaluatorRole.PROFESSOR);

        when(referentService.createReferent(ArgumentMatchers.any(ReferentDto.class))).thenReturn(saved);

        ReferentDto request = new ReferentDto(
                null,
                "Alice",
                "Smith",
                "alice@example.com",
                "555-1234",
                AcademicLevel.MASTER_UNI,
                EvaluatorRole.PROFESSOR
        );

        // Act & Assert
        mockMvc.perform(post("/referent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/referent/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Alice"));
    }

    @Test
    void getReferent_returnsDto() throws Exception {
        Referent saved = new Referent();
        saved.setId(1L);
        saved.setFirstName("Alice");
        saved.setLastName("Smith");
        saved.setEmail("alice@example.com");
        saved.setPhoneNumber("555-1234");
        saved.setAcademicLevel(AcademicLevel.MASTER_UNI);
        saved.setRole(EvaluatorRole.PROFESSOR);

        when(referentService.findById(1L)).thenReturn(saved);

        mockMvc.perform(get("/referent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Alice"));
    }
}
