package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.student.StudentCreateDTO;
import com.example.profisbackend.model.Student;

public class StudentMapper {
    public static Student studentCreateDTO_TO_Student_Mapper(StudentCreateDTO studentCreateDTO) {
        Student student = new Student();
        student.setEmail(studentCreateDTO.email());
        student.setFirstName(studentCreateDTO.firstname());
        student.setLastName(studentCreateDTO.lastname());
        return student;
    }
}
