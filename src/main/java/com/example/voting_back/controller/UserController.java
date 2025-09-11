package com.example.voting_back.controller;

import com.example.voting_back.dto.response.LoginResponse;
import com.example.voting_back.dto.response.VoteResponse;
import com.example.voting_back.service.UserService;
import com.example.voting_back.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<?> getProfile(@AuthenticationPrincipal LoginResponse.UserDto user) {
        List<VoteResponse> list = userService.listMyCreated(user.id());
        return ApiResponse.success(list);
    }

    @PutMapping("/me")
    public ApiResponse<?> updateProfile(
            @AuthenticationPrincipal LoginResponse.UserDto user,
            @RequestBody Map<String, String> updates) {
        List<VoteResponse> list = userService.listMyCreated(user.id());
        return ApiResponse.success(list);
    }

    @GetMapping("/created")
    public ApiResponse<List<VoteResponse>> myCreated(@AuthenticationPrincipal LoginResponse.UserDto user) {
        List<VoteResponse> list = userService.listMyCreated(user.id());
        return ApiResponse.success(list);
    }

    @GetMapping("/participated")
    public ApiResponse<List<VoteResponse>> myParticipated(@AuthenticationPrincipal LoginResponse.UserDto user) {
        List<VoteResponse> list = userService.listMyParticipated(user.id());
        return ApiResponse.success(list);
    }
}
