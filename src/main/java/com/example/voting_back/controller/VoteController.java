package com.example.voting_back.controller;

import com.example.voting_back.dto.request.VoteRequest;
import com.example.voting_back.dto.response.VoteResponse;
import com.example.voting_back.dto.user.UserDto;
import com.example.voting_back.service.VoteService;
import com.example.voting_back.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/new")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Long> createVote(
            @AuthenticationPrincipal UserDto user,
            @RequestBody VoteRequest req
    ) {
        Long userId = user == null ? null : user.id();
        Long id = voteService.create(req, userId);
        return ApiResponse.ok(id);
    }

    @GetMapping("/list")
    public ApiResponse<Page<VoteResponse>> listVote(
            @AuthenticationPrincipal UserDto user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        Long userId = user == null ? null : user.id();
        Page<VoteResponse> list = voteService.listAll(page, size, userId);
        return ApiResponse.ok(list);
    }

    @PostMapping("/cast")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<VoteResponse> castVote(
            @AuthenticationPrincipal UserDto user,
            @PathVariable Long voteId,
            @PathVariable Long optionId
    ) {
        Long userId = user == null ? null : user.id();
        VoteResponse result = voteService.castVote(userId, voteId, optionId);
        return ApiResponse.ok(result);
    }
}

