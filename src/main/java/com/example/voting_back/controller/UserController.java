package com.example.voting_back.controller;

import com.example.voting_back.dto.user.LoginResponse;
import com.example.voting_back.service.UserService;
import com.example.voting_back.common.ApiResponse;
import com.example.voting_back.dto.user.LoginRequest;
import com.example.voting_back.dto.user.UserDto;
import com.example.voting_back.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@RequestBody LoginRequest req) {
        if (req == null) {
            return ApiResponse.error(400, "INVALID_INPUT");
        }

        try {
            UserDto dto = userService.register(req.username(), req.password());

            String token = jwtUtil.generateToken(dto.id(), dto.username());

            return ApiResponse.ok(new LoginResponse(token, dto));
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(500, "ERROR");
        }
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest req) {
        if (req == null) {
            return ApiResponse.error(400, "INVALID_INPUT");
        }

        UserDto dto = userService.login(req.username(), req.password());

        String token = jwtUtil.generateToken(dto.id(), dto.username());

        return ApiResponse.ok(new LoginResponse(token, dto));
    }
}
