package com.example.profisbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.profisbackend.service.InputOutputService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
@AllArgsConstructor
@Controller
@RequestMapping("/api/excel")
public class InputOutputController {
    private InputOutputService inputOutputService;

    @PostMapping("/import")
    public ResponseEntity<String> postMethodName( @RequestParam String file) {
        return inputOutputService.readInputFile(file);
    }  
}