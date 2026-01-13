package com.example.profisbackend.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }

    //this redirects paths like /asdjad to index. Path like /sdksd/akdad (depth 2) are not redirected, and it's ok.
    @GetMapping("/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/index.html";
    }
}