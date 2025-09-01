package com.example.voting_back.controller;

import com.example.voting_back.dto.user.UserDto;
import com.example.voting_back.dto.votelist.VotePage;
import com.example.voting_back.service.VoteService;
import com.example.voting_back.common.ApiResponse;
import com.example.voting_back.dto.*;
import com.example.voting_back.dto.votelist.VoteListItem;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<CreateVoteResponse> createVote(@RequestBody CreateVoteRequest req) {
        Long id = voteService.create(req);
        return ApiResponse.ok(new CreateVoteResponse(id));
    }

    @GetMapping("/list")
    public ApiResponse<VotePage<VoteListItem>> listVote(
            @AuthenticationPrincipal UserDto user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        Long userId = user == null ? null : user.id();
        VotePage<VoteListItem> list = voteService.listAll(page, size, userId);
        return ApiResponse.ok(list);
    }

    @PostMapping("/cast")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<CastVoteResponse> castVote(@RequestBody CastVoteRequest req) {
        CastVoteResponse result = voteService.castVote(req);
        return ApiResponse.ok(result);
    }
}

