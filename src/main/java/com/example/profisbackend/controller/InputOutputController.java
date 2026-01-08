package com.example.profisbackend.controller;

import com.example.profisbackend.service.OutputService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.profisbackend.service.InputService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/excel")
public class InputOutputController {
    private final InputService inputOutputService;
    private  final OutputService outputService;

    @PostMapping("/import")
    public ResponseEntity<String> postMethodName( @RequestParam String file) {
        return inputOutputService.copyToDb(file);
    }
    @GetMapping("/export")
    public ResponseEntity<byte[]> postMethodName(  ) throws IOException {
        return outputService.exportDatabaseToExcel();
    }
}