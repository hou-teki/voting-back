package com.example.voting_back.controller;

import com.example.voting_back.dto.response.VoteResponse;
import com.example.voting_back.service.UserService;
import com.example.voting_back.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/created")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<VoteResponse>> myCreated(@RequestParam Long userId) {
        List<VoteResponse> list = userService.listMyCreated(userId);
        return ApiResponse.success(list);
    }

    @GetMapping("/participated")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<VoteResponse>> myParticipated(@RequestParam Long userId) {
        List<VoteResponse> list = userService.listMyParticipated(userId);
        return ApiResponse.success(list);
    }
}
