package com.example.profisbackend.controller;

import com.example.profisbackend.dto.reports.SwsReportDTO;
import com.example.profisbackend.service.PdfService;
import com.example.profisbackend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sws")
public class SwsController {
    final private StatisticsService statisticsService;
    final private PdfService pdfService;

    @GetMapping("/main-user/current")
    public ResponseEntity<Double> getSwsInCurrentSemesterForMainUser(){
        return ResponseEntity.ok(statisticsService.getSwsInCurrentSemesterForMainUser());
    }
    @GetMapping("/main-user/{semester}")
    public ResponseEntity<Double> getSwsInGivenSemesterForMainUser(@PathVariable String semester){
        return ResponseEntity.ok(statisticsService.getSwsInGivenSemesterForMainUser(semester));
    }
    @GetMapping("/{evaluatorId}/current")
    public ResponseEntity<Double> getSwsInCurrentSemesterForEvaluator(@PathVariable Long evaluatorId){
        return ResponseEntity.ok(statisticsService.getSwsInCurrentSemesterForEvaluatorByEvaluatorId(evaluatorId));
    }
    @GetMapping("/{evaluatorId}/{semester}")
    public ResponseEntity<Double> getSwsInGivenSemesterForEvaluator(@PathVariable Long evaluatorId, @PathVariable String semester){
        return ResponseEntity.ok(statisticsService.getSwsInGivenSemesterForEvaluatorByEvaluatorId(evaluatorId, semester));
    }
    @GetMapping("/main-user/report/{semester}")
    public ResponseEntity<byte[]> getSwsReportForMainUser(@PathVariable String semester){
        Optional<SwsReportDTO> reportDTO = statisticsService.getSwsReportForMainUserBySemester(semester);
        if(reportDTO.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        try {
            return ResponseEntity.ok(pdfService.generatePdfSwsReport(reportDTO.get()));
        } catch (IOException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
