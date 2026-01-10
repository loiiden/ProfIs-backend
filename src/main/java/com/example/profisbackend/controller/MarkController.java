package com.example.profisbackend.controller;

import com.example.profisbackend.dto.components.MarksComponentHomePageDTO;
import com.example.profisbackend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/mark")
public class MarkController {
    final public StatisticsService statisticsService;
    @GetMapping("/info")
    public ResponseEntity<MarksComponentHomePageDTO> getDataFromMarksComponentHomePage() {
        return ResponseEntity.ok(statisticsService.getDataForMarksComponentHomePage());
    }
}
