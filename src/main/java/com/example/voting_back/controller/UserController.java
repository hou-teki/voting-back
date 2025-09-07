package com.example.voting_back.controller;

import com.example.voting_back.dto.response.VoteResponse;
import com.example.voting_back.service.MyVoteService;
import com.example.voting_back.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class UserController {

    private final MyVoteService myVoteService;

    @GetMapping("/created")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<VoteResponse>> myCreated(@RequestParam Long userId) {
        List<VoteResponse> list = myVoteService.listMyCreated(userId);
        return ApiResponse.ok(list);
    }

    @GetMapping("/participated")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<VoteResponse>> myParticipated(@RequestParam Long userId) {
        List<VoteResponse> list = myVoteService.listMyParticipated(userId);
        return ApiResponse.ok(list);
    }
}
