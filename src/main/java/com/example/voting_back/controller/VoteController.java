package com.example.voting_back.controller;

import com.example.voting_back.dto.request.VoteRequest;
import com.example.voting_back.dto.response.UserResponse;
import com.example.voting_back.dto.response.VoteResponse;
import com.example.voting_back.service.VoteService;
import com.example.voting_back.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/new")
    public ApiResponse<Long> createVote(
            @AuthenticationPrincipal UserResponse.UserProfileDto user,
            @Valid @RequestBody VoteRequest req
    ) {
        Long id = voteService.create(req, user.id());
        return ApiResponse.success(id);
    }

    @GetMapping("/list")
    public ApiResponse<Page<VoteResponse>> listVote(
            @AuthenticationPrincipal UserResponse.UserProfileDto user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        Long userId = user == null ? null : user.id();
        Page<VoteResponse> list = voteService.listAll(page, size, userId);
        return ApiResponse.success(list);
    }

    @PostMapping("/cast/{voteId}/{optionId}")
    public ApiResponse<VoteResponse> castVote(
            @AuthenticationPrincipal UserResponse.UserProfileDto user,
            @PathVariable Long voteId,
            @PathVariable Long optionId
    ) {
        VoteResponse result = voteService.castVote(user.id(), voteId, optionId);
        return ApiResponse.success(result);
    }
}
