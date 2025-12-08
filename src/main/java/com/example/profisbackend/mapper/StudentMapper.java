package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.student.StudentCreateDTO;
import com.example.profisbackend.model.Student;

public class StudentMapper {
    public static Student studentCreateDTO_TO_Student_Mapper(StudentCreateDTO studentCreateDTO) {
        Student student = new Student();
        student.setFirstName(studentCreateDTO.firstName());
        student.setLastName(studentCreateDTO.lastName());
        student.setAddress(studentCreateDTO.address());
        student.setEmail(studentCreateDTO.email());
        student.setPhoneNumber(studentCreateDTO.phoneNumber());
        student.setAcademicLevel(studentCreateDTO.academicLevel());

        return student;
    }
}
