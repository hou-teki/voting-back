package com.example.voting_back.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("api/hello")
    public String hello() {
        return "Hello World!";
    }
}
