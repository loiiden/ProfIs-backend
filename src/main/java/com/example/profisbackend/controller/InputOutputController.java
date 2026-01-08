package com.example.profisbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.profisbackend.service.InputService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/excel")
public class InputOutputController {
    private final InputService inputOutputService;

    @PostMapping("/import")
    public ResponseEntity<String> postMethodName( @RequestParam String file) {
        return inputOutputService.copyToDb(file);
    }  
}