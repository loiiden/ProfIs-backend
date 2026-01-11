package com.example.profisbackend.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.profisbackend.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/data")
public class DataController {
    private final DataService inputOutputService;


    @PostMapping(value = "/import",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importData(@RequestPart MultipartFile excelFile) {
        return inputOutputService.copyToDb(excelFile);
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