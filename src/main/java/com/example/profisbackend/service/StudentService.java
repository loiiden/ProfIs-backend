package com.example.profisbackend.service;

import com.example.profisbackend.dto.student.StudentCreateDTO;
import com.example.profisbackend.dto.student.StudentMileStoneDTO;
import com.example.profisbackend.dto.student.StudentPatchDTO;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.entities.Student;
import com.example.profisbackend.entities.StudyProgram;
import com.example.profisbackend.exceptions.StudentNotFoundException;
import com.example.profisbackend.mapper.StudentMapper;
import com.example.profisbackend.model.StudentMileStone;
import com.example.profisbackend.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public void deleteStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
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
        if (studentPatchDTO.phoneNumber() != null) {
            student.setPhoneNumber(studentPatchDTO.phoneNumber());
        }
        if (studentPatchDTO.academicLevel() != null) {
            student.setAcademicLevel(studentPatchDTO.academicLevel());
        }
        if (studentPatchDTO.studentNumber() != null) {
            student.setStudentNumber(studentPatchDTO.studentNumber());
        }
        return studentRepository.save(student);
    }

    public List<StudentMileStone> getStudentMileStones(Long ID){
        Student student=getStudentById(ID);
        List<ScientificWork> scientificWorks=student.getScientificWorks();
        List<StudentMileStone> mileStones= new ArrayList<>();
        for (ScientificWork x: scientificWorks){
          mileStones.add(aggreagteToStudentMileStone(x))  ;
        }
        return mileStones;

    }

     private StudentMileStone aggreagteToStudentMileStone(ScientificWork scientificWork) {
        StudyProgram studyProgram = scientificWork.getStudyProgram();
        String studyProgramTitle;
        if (studyProgram == null) {
            studyProgramTitle = "";

        } else {
            studyProgramTitle = studyProgram.getTitle();
        }
        String scientificWorkTitle = scientificWork.getTitle();
        return new StudentMileStone(scientificWorkTitle, studyProgramTitle);
    }


}
