package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.scientificWork.ScientificWorkCreateDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkResponseDTO;
import com.example.profisbackend.dto.student.StudentCreateDTO;
import com.example.profisbackend.model.ScientificWork;

public class ScientificWorkMapper {
    public static ScientificWorkResponseDTO convertToResponseDTO(ScientificWork scientificWork) {
        return new ScientificWorkResponseDTO(
                scientificWork.getId(),
                scientificWork.getColloquium(),
                scientificWork.getTitle(),
                scientificWork.getStartDate(),
                scientificWork.getEndDate(),
                scientificWork.getStudent().getId(),
                scientificWork.getStudyProgram().getId()
        );
    }
}
