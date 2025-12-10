package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.student.StudentCreateDTO;
import com.example.profisbackend.dto.student.StudentResponseDTO;
import com.example.profisbackend.model.ScientificWork;
import com.example.profisbackend.model.Student;

import java.util.List;

public class StudentMapper {
    public static Student toStudent(StudentCreateDTO studentCreateDTO) {
        Student student = new Student();
        student.setFirstName(studentCreateDTO.firstName());
        student.setLastName(studentCreateDTO.lastName());
        student.setAddress(studentCreateDTO.address());
        student.setEmail(studentCreateDTO.email());
        student.setPhoneNumber(studentCreateDTO.phoneNumber());
        student.setStudentNumber(studentCreateDTO.studentNumber());
        student.setAcademicLevel(studentCreateDTO.academicLevel());

        return student;
    }
    public static StudentResponseDTO toStudentResponseDTO(Student student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getAddress(),
                student.getEmail(),
                student.getPhoneNumber(),
                student.getStudentNumber(),
                student.getAcademicLevel(),
                student.getScientificWorks().stream().map((scientificWork -> scientificWork.getId())).toList()
        );
    }
}
