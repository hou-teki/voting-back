package com.example.voting_back.controller;

import com.example.voting_back.dto.request.UserRequest;
import com.example.voting_back.dto.response.UserResponse;
import com.example.voting_back.dto.response.VoteResponse;
import com.example.voting_back.service.UserService;
import com.example.voting_back.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserResponse.UserProfileDto> getProfile(
            @AuthenticationPrincipal UserResponse.UserProfileDto user) {
        UserResponse.UserProfileDto dto = userService.getProfile(user.id());
        return ApiResponse.success(dto);
    }

    @PostMapping("/me")
    public ApiResponse<UserResponse.UserProfileDto> updateProfile(
            @AuthenticationPrincipal UserResponse.UserProfileDto user,
            @Valid @RequestBody UserRequest.UserProfileRequest updates) {
        UserResponse.UserProfileDto dto = userService.updateProfile(user.id(), updates);
        return ApiResponse.success(dto);
    }

    @GetMapping("/created")
    public ApiResponse<Page<VoteResponse>> myCreated(
            @AuthenticationPrincipal UserResponse.UserProfileDto user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {
        Page<VoteResponse> list = userService.listMyCreated(page, size, user.id());
        return ApiResponse.success(list);
    }

    @GetMapping("/participated")
    public ApiResponse<Page<VoteResponse>> myParticipated(
            @AuthenticationPrincipal UserResponse.UserProfileDto user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        Page<VoteResponse> list = userService.listMyParticipated(page, size, user.id());
        return ApiResponse.success(list);
    }
}
