package com.example.profisbackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.entities.Student;
import com.example.profisbackend.entities.StudyProgram;
import com.example.profisbackend.model.StudentMileStone;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentMileStoneService {

    private final StudentService studentService;

    private StudentMileStone aggregateToStudentCard(ScientificWork scientificWork) {
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

    public List<StudentMileStone> getStudentMileStones(Long id) {
        Student student = studentService.getStudentById(id);
        List<ScientificWork> scientificWorks = student.getScientificWorks(); // stores all scientificorks
        List<StudentMileStone> studentMileStones = new ArrayList<>();

        for (ScientificWork x : scientificWorks) {
            studentMileStones.add(aggregateToStudentCard(x));
        }
        return studentMileStones;
    }

}
