package com.example.profisbackend.service;

import com.example.profisbackend.dto.student.StudentCreateDTO;
import com.example.profisbackend.dto.student.StudentPatchDTO;
import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.exceptions.StudentNotFoundException;
import com.example.profisbackend.mapper.StudentMapper;
import com.example.profisbackend.model.Student;
import com.example.profisbackend.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public Student createStudent(StudentCreateDTO studentDTO) {
        return studentRepository.save(StudentMapper.studentCreateDTO_TO_Student_Mapper(studentDTO));
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }
    public boolean deleteStudentById(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public Student patchStudentById(Long id, StudentPatchDTO studentPatchDTO) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));

        if (studentPatchDTO.firstName() != null) {
            student.setFirstName(studentPatchDTO.firstName());
        }
        if (studentPatchDTO.lastName() != null) {
            student.setLastName(studentPatchDTO.lastName());
        }
        if (studentPatchDTO.address() != null) {
            student.setAddress(studentPatchDTO.address());
        }
        if (studentPatchDTO.email() != null) {
            student.setEmail(studentPatchDTO.email());
        }
        if(studentPatchDTO.phoneNumber() != null) {
            student.setPhoneNumber(studentPatchDTO.phoneNumber());
        }
        if(studentPatchDTO.academicLevel() != null) {
            student.setAcademicLevel(studentPatchDTO.academicLevel());
        }
        if(studentPatchDTO.studentNumber() != null) {
            student.setStudentNumber(studentPatchDTO.studentNumber());
        }
        return studentRepository.save(student);
    }
}
