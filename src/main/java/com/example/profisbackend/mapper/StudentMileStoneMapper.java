package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.student.StudentMileStoneDTO;
import com.example.profisbackend.model.StudentMileStone;

public class StudentMileStoneMapper {

    public  static StudentMileStoneDTO toDto(StudentMileStone studentMileStone){
        return new StudentMileStoneDTO(studentMileStone.getStudyProgramTitle(),studentMileStone.getScientificWorkTitle());
    }
    
}
