package com.example.profisbackend.service;

import com.example.profisbackend.dto.scientificWork.ScientificWorkCreateDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkPatchDTO;
import com.example.profisbackend.model.ScientificWork;
import com.example.profisbackend.model.Student;
import com.example.profisbackend.model.StudyProgram;
import com.example.profisbackend.repository.ScientificWorkRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScientificWorkService {
    private final ScientificWorkRepository scientificWorkRepository;
    private final StudentService studentService;
    private final StudyProgramService studyProgramService;

    public ScientificWork findById(Long id) {
        return scientificWorkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Scientific Work Not Found. ID: " + id));
    }

    public ScientificWork create(ScientificWorkCreateDTO scientificWorkCreateDTO) {
        // Creates a scientific work
        ScientificWork scientificWork = new ScientificWork();
        scientificWork.setColloquium(scientificWorkCreateDTO.colloquium());
        scientificWork.setTitle(scientificWorkCreateDTO.title());
        scientificWork.setStartDate(scientificWorkCreateDTO.startDate());
        scientificWork.setEndDate(scientificWorkCreateDTO.endDate());

        // Student must already exit it will be retrieved from the database
        Student student = studentService.getStudentById(scientificWorkCreateDTO.studentId());
        // A Scientificwork contains a Student
        scientificWork.setStudent(student);
        // One Student can have mutliple scientificworks
        student.getScientificWorks().add(scientificWork);

        StudyProgram studyProgram = studyProgramService.getStudyProgramById(scientificWorkCreateDTO.studyProgramId());

        scientificWork.setStudyProgram(studyProgram);// Scientificwork refers to a studyprogram
        scientificWorkRepository.save(scientificWork);

        return scientificWork;
    }

    public ScientificWork patchScientificWorkById(Long id, ScientificWorkPatchDTO scientificWorkPatchDTO) {
        ScientificWork scientificWork = findById(id);
        if (scientificWorkPatchDTO.colloquium() != null) {
            scientificWork.setColloquium(scientificWorkPatchDTO.colloquium());
        }
        if (scientificWorkPatchDTO.title() != null) {
            scientificWork.setTitle(scientificWorkPatchDTO.title());
        }
        if (scientificWorkPatchDTO.startDate() != null) {
            scientificWork.setStartDate(scientificWorkPatchDTO.startDate());
        }
        if (scientificWorkPatchDTO.endDate() != null) {
            scientificWork.setEndDate(scientificWorkPatchDTO.endDate());
        }
        if (scientificWorkPatchDTO.studentId() != null) {
            Student newStudent = studentService.getStudentById(scientificWorkPatchDTO.studentId());
            scientificWork.setStudent(newStudent);
        }
        if (scientificWorkPatchDTO.studyProgramId() != null) {
            StudyProgram newStudyProgram = studyProgramService
                    .getStudyProgramById(scientificWorkPatchDTO.studyProgramId());
            scientificWork.setStudyProgram(newStudyProgram);
        }
        scientificWorkRepository.save(scientificWork);
        return scientificWork;
    }

    public void deleteById(Long id) {
        ScientificWork scientificWork = findById(id);
        scientificWorkRepository.deleteById(scientificWork.getId());
    }

    /*
     * DO IT IN NOTE SERVICE
     * public void addNote()
     */
    public List<ScientificWork> findAll() {
        return scientificWorkRepository.findAll();
    }
}
