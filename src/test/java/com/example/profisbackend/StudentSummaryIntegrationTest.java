package com.example.profisbackend;

import com.example.profisbackend.dto.StudentSummaryDTO;
import com.example.profisbackend.dto.StudyProgramDto;
import com.example.profisbackend.dto.scientificWork.ScientificWorkCreateDTO;
import com.example.profisbackend.dto.student.StudentCreateDTO;
import com.example.profisbackend.enums.AcademicLevel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentSummaryIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void summaryEndpoint_returnsStudentWithWorks() {
        // Create student
        StudentCreateDTO studentCreate = new StudentCreateDTO("Anna","Smith","Addr","anna@example.com","123",11111L, AcademicLevel.MASTER_UNI);
        ResponseEntity<Object> studentResp = restTemplate.postForEntity("/api/student", studentCreate, Object.class);
        assertThat(studentResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Extract id from response body map via GET /api/student list
        ResponseEntity<Object[]> allResp = restTemplate.getForEntity("/api/student", Object[].class);
        assertThat(allResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Object[] list = allResp.getBody();
        assertThat(list).isNotEmpty();
        Object last = list[list.length - 1];
        @SuppressWarnings("unchecked")
        var map = (java.util.Map<String,Object>) last;
        Number idNum = (Number) map.get("id");
        Long studentId = idNum.longValue();

        // Create StudyProgram required by ScientificWork
        StudyProgramDto sp = new StudyProgramDto("Computer Science", 4.0f);
        ResponseEntity<Object> spResp = restTemplate.postForEntity("/api/study-program", sp, Object.class);
        assertThat(spResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        @SuppressWarnings("unchecked")
        var spMap = (java.util.Map<String,Object>) spResp.getBody();
        Number spIdNum = (Number) spMap.get("id");
        Long studyProgramId = spIdNum.longValue();

        // Create a scientific work for this student
        ScientificWorkCreateDTO swCreate = new ScientificWorkCreateDTO(LocalDateTime.of(2024,6,1,10,0), "My Thesis", LocalDate.of(2024,1,1), LocalDate.of(2024,12,31), studentId, studyProgramId);
        ResponseEntity<Object> swResp = restTemplate.postForEntity("/api/scientific-work", swCreate, Object.class);
        assertThat(swResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Call summary endpoint
        ResponseEntity<StudentSummaryDTO> summaryResp = restTemplate.getForEntity("/api/student/" + studentId + "/summary", StudentSummaryDTO.class);
        assertThat(summaryResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        StudentSummaryDTO summary = summaryResp.getBody();
        assertThat(summary).isNotNull();
        assertThat(summary.id()).isEqualTo(studentId);
        assertThat(summary.scientificWorks()).isNotEmpty();
    }
}
