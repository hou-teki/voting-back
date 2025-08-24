package com.example.voting_back.controller;

import com.example.voting_back.service.UserService;
import com.example.voting_back.common.ApiResponse;
import com.example.voting_back.dto.LoginRequest;
import com.example.voting_back.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserDto> register(@RequestBody LoginRequest req) {
        if (req == null) {
            return ApiResponse.error(400, "INVALID_INPUT");
        }

        try {
            UserDto dto = userService.register(req.username(), req.password());
            return ApiResponse.ok(dto);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(500, "ERROR");
        }
    }

    @PostMapping("/login")
    public ApiResponse<UserDto> login(@RequestBody LoginRequest req) {
        if (req == null) {
            return ApiResponse.error(400, "INVALID_INPUT");
        }

        UserDto dto = userService.login(req.username(), req.password());
        return ApiResponse.ok(dto);
    }
}
