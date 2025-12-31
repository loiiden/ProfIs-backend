package com.example.profisbackend.service;

import com.example.profisbackend.dto.student.StudentCreateDTO;
import com.example.profisbackend.dto.student.StudentPatchDTO;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.entities.Student;
import com.example.profisbackend.mapper.StudentMapper;
import com.example.profisbackend.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public Student createStudent(StudentCreateDTO studentDTO) {
        return studentRepository.save(StudentMapper.toStudent(studentDTO));
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student findStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Student not found " + id));
    }

    public void deleteStudentById(Long id) {
        Student student = findStudentById(id);
        studentRepository.delete(student);
    }

    public Student patchStudentById(Long id, StudentPatchDTO studentPatchDTO) {
        Student student = findStudentById(id);

        student.setFirstName(studentPatchDTO.firstName());
        student.setLastName(studentPatchDTO.lastName());
        student.setAddress(studentPatchDTO.address());
        student.setEmail(studentPatchDTO.email());
        student.setPhoneNumber(studentPatchDTO.phoneNumber());
        student.setAcademicLevel(studentPatchDTO.academicLevel());
        student.setStudentNumber(studentPatchDTO.studentNumber());
        student.setSalutation(studentPatchDTO.salutation());
        return studentRepository.save(student);
    }
    public List<ScientificWork> getStudentsScientificWorksByStudentId(Long studentId) {
        Student student = findStudentById(studentId);
        return student.getScientificWorks();
    }
}
