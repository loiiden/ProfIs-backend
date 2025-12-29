package com.example.profisbackend.controller;

import com.example.profisbackend.dto.student.StudentCreateDTO;
import com.example.profisbackend.dto.student.StudentMileStoneDTO;
import com.example.profisbackend.dto.student.StudentPatchDTO;
import com.example.profisbackend.dto.student.StudentResponseDTO;
import com.example.profisbackend.entities.Student;
import com.example.profisbackend.mapper.StudentMapper;
import com.example.profisbackend.mapper.StudentMileStoneMapper;
import com.example.profisbackend.service.StudentMileStoneService;
import com.example.profisbackend.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/student")
public class StudentController {
    private final StudentService studentService;
    private final StudentMileStoneService mileStoneService;
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody StudentCreateDTO studentDTO) {
        return ResponseEntity.ok(studentService.createStudent(studentDTO));
    }
    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents().stream().map(student -> StudentMapper.toStudentResponseDTO(student)).toList());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> patchStudentById(@PathVariable Long id, @RequestBody StudentPatchDTO studentPatchDTO) {
        return ResponseEntity.ok(StudentMapper.toStudentResponseDTO(studentService.patchStudentById(id, studentPatchDTO)));
    }
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(StudentMapper.toStudentResponseDTO(studentService.getStudentById(id)));
    }
    @GetMapping("/{id}/milestones")
    public ResponseEntity<List<StudentMileStoneDTO>> getStudentMilestones(@PathVariable Long id) {
        return ResponseEntity.ok(mileStoneService.getStudentMileStones(id).stream().map(StudentMileStoneMapper::toDto).collect(Collectors.toList()));
    }
    
}
