package com.example.profisbackend.service;

import com.example.profisbackend.dto.student.StudentCreateDTO;
import com.example.profisbackend.dto.student.StudentPatchDTO;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.entities.Student;
import com.example.profisbackend.entities.StudyProgram;
import com.example.profisbackend.mapper.StudentMapper;
import com.example.profisbackend.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final StudyProgramService studyProgramService;

    public Student createStudent(StudentCreateDTO studentDTO) {
        return studentRepository.save(studentMapper.toStudent(studentDTO));
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student findStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Student not found " + id));
    }

    public Student findStudentByStudentNumber(Long studentNumber) {
        return studentRepository.findStudentByStudentNumber(studentNumber).orElseThrow(() -> new EntityNotFoundException("Student not found " + studentNumber));
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
        StudyProgram studyProgram = null;
        if (studentPatchDTO.studyProgramId() != null) {
            studyProgram = studyProgramService.findById(studentPatchDTO.studyProgramId());
        }
        student.setStudyProgram(studyProgram);

        return studentRepository.save(student);
    }

    public List<ScientificWork> getStudentsScientificWorksByStudentId(Long studentId) {
        Student student = findStudentById(studentId);
        return student.getScientificWorks();
    }

    public Student findByStudentNumber(Long studentNumber) {
        return studentRepository.findStudentByStudentNumber(studentNumber).orElseThrow(() -> new EntityNotFoundException("Student not found " + studentNumber));
    }

    public boolean existsByStudentNumber(Long studentNumber) {
        return studentRepository.existsByStudentNumber(studentNumber);
    }
}
