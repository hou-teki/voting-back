package com.example.voting_back.controller;

import com.example.voting_back.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/api/hello")
    public ApiResponse<String> hello() {
        return ApiResponse.ok("Hello World!");
    }
}
