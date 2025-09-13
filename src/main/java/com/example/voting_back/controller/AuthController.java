package com.example.voting_back.controller;

import com.example.voting_back.dto.request.UserRequest;
import com.example.voting_back.dto.response.UserResponse;
import com.example.voting_back.service.AuthService;
import com.example.voting_back.common.ApiResponse;
import com.example.voting_back.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserRequest req) {
        UserResponse.UserProfileDto dto = authService.register(req.username(), req.password());
        String token = jwtUtil.generateToken(dto.id(), dto.username());
        return ApiResponse.success(new UserResponse(token, dto));
    }

    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@Valid @RequestBody UserRequest req) {
        UserResponse.UserProfileDto dto = authService.login(req.username(), req.password());
        String token = jwtUtil.generateToken(dto.id(), dto.username());
        return ApiResponse.success(new UserResponse(token, dto));
    }
}
