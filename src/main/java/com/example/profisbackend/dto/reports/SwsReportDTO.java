package com.example.profisbackend.dto.reports;

import com.example.profisbackend.dto.scientificWork.ScientificWorkSwsReportDTO;

import java.util.List;

public record SwsReportDTO(
        String evaluatorFullName,
        String semester,
        List<ScientificWorkSwsReportDTO> works,
        Double totalSws
) {
}
