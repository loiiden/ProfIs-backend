package com.example.profisbackend.controller;

import com.example.profisbackend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sws")
public class SwsController {
    final private StatisticsService statisticsService;

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
}
