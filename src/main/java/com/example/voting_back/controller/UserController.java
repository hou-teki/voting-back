package com.example.voting_back.controller;

import com.example.voting_back.dto.response.LoginResponse;
import com.example.voting_back.dto.response.VoteResponse;
import com.example.voting_back.service.UserService;
import com.example.voting_back.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<?> getProfile(@AuthenticationPrincipal LoginResponse.UserDto user) {
        return null;
    }

    @PutMapping("/me")
    public ApiResponse<?> updateProfile(
            @AuthenticationPrincipal LoginResponse.UserDto user,
            @RequestBody Map<String, String> updates) {
        return null;
    }

    @GetMapping("/created")
    public ApiResponse<Page<VoteResponse>> myCreated(
            @AuthenticationPrincipal LoginResponse.UserDto user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {
        Page<VoteResponse> list = userService.listMyCreated(page, size, user.id());
        return ApiResponse.success(list);
    }

    @GetMapping("/participated")
    public ApiResponse<Page<VoteResponse>> myParticipated(
            @AuthenticationPrincipal LoginResponse.UserDto user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        Page<VoteResponse> list = userService.listMyParticipated(page, size, user.id());
        return ApiResponse.success(list);
    }
}
