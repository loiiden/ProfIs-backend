package com.example.profisbackend.controller;

import com.example.profisbackend.dto.student.StudentCreateDTO;
import com.example.profisbackend.dto.student.StudentPatchDTO;
import com.example.profisbackend.model.Student;
import com.example.profisbackend.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/student")
public class StudentController {
    private final StudentService studentService;
    @PostMapping("")
    public ResponseEntity<Student> createStudent(@RequestBody StudentCreateDTO studentDTO) {
        return ResponseEntity.ok(studentService.createStudent(studentDTO));
    }
    @GetMapping("")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Student> patchStudentById(@PathVariable Long id, @RequestBody StudentPatchDTO studentPatchDTO) {
        return ResponseEntity.ok(studentService.patchStudentById(id, studentPatchDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }
}
