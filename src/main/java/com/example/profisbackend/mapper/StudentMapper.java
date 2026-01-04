package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.student.StudentCreateDTO;
import com.example.profisbackend.dto.student.StudentResponseDTO;
import com.example.profisbackend.entities.Student;
import com.example.profisbackend.entities.StudyProgram;
import com.example.profisbackend.service.StudyProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapper {
    final StudyProgramService studyProgramService;
    public Student toStudent(StudentCreateDTO studentCreateDTO) {
        Student student = new Student();
        student.setFirstName(studentCreateDTO.firstName());
        student.setLastName(studentCreateDTO.lastName());
        student.setAddress(studentCreateDTO.address());
        student.setEmail(studentCreateDTO.email());
        student.setPhoneNumber(studentCreateDTO.phoneNumber());
        student.setStudentNumber(studentCreateDTO.studentNumber());
        student.setAcademicLevel(studentCreateDTO.academicLevel());
        student.setSalutation(studentCreateDTO.salutation());
        StudyProgram studyProgram = null;
        if (studentCreateDTO.studyProgramId() != null) {
            studyProgram = studyProgramService.findById(studentCreateDTO.studyProgramId());
        }
        student.setStudyProgram(studyProgram);

        return student;
    }

    public static StudentResponseDTO toStudentResponseDTO(Student student) {
        Long studyProgramId;
        StudyProgram studyProgram = student.getStudyProgram();
        if (studyProgram != null) {
           studyProgramId = studyProgram.getId();
        }else{
            studyProgramId = null;
        }
        return new StudentResponseDTO(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getAddress(),
                student.getEmail(),
                student.getPhoneNumber(),
                student.getStudentNumber(),
                student.getSalutation(),
                student.getAcademicLevel(),
                student.getScientificWorks().stream().map((scientificWork -> scientificWork.getId())).toList(),
                studyProgramId
        );
    }
}