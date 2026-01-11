package com.example.profisbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.profisbackend.service.DataService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/data")
public class DataController {
    private final DataService inputOutputService;


    @PostMapping("/import")
    public ResponseEntity<String> importData(@RequestParam String file) {
        return inputOutputService.copyToDb(file);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() throws IOException {
        return inputOutputService.exportDatabaseToExcel();
    }

    @DeleteMapping("/reset")
    public ResponseEntity<String> resetData() throws IOException {
        return inputOutputService.resetDatabase();
    }

}