package com.example.voting_back.controller;

import com.example.voting_back.dto.request.LoginRequest;
import com.example.voting_back.dto.response.LoginResponse;
import com.example.voting_back.service.UserService;
import com.example.voting_back.common.ApiResponse;
import com.example.voting_back.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@Valid @RequestBody LoginRequest req) {
        LoginResponse.UserDto dto = userService.register(req.username(), req.password());
        String token = jwtUtil.generateToken(dto.id(), dto.username());
        return ApiResponse.ok(new LoginResponse(token, dto));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        LoginResponse.UserDto dto = userService.login(req.username(), req.password());
        String token = jwtUtil.generateToken(dto.id(), dto.username());
        return ApiResponse.ok(new LoginResponse(token, dto));
    }
}
