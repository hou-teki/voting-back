package com.example.voting_back.controller;

import com.example.voting_back.dto.votelist.VotePage;
import com.example.voting_back.service.VoteService;
import com.example.voting_back.common.ApiResponse;
import com.example.voting_back.dto.*;
import com.example.voting_back.dto.votelist.VoteListItem;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/new")
    public ApiResponse<CreateVoteResponse> createVote(@RequestBody CreateVoteRequest req) {
        Long id = voteService.create(req);
        return ApiResponse.ok(new CreateVoteResponse(id));
    }

    @GetMapping("/list")
    public ApiResponse<VotePage<VoteListItem>> listVote(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(required = false) Long userId
    ) {
        VotePage<VoteListItem> list = voteService.listAll(page, size, userId);
        return ApiResponse.ok(list);
    }

    @PostMapping("/cast")
    public ApiResponse<CastVoteResponse> castVote(@RequestBody CastVoteRequest req) {
        CastVoteResponse result = voteService.castVote(req);
        return ApiResponse.ok(result);
    }
}

