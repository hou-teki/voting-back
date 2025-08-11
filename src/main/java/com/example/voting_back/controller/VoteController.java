package com.example.voting_back.controller;

import com.example.voting_back.Service.VoteService;
import com.example.voting_back.common.ApiResponse;
import com.example.voting_back.dto.CreateVoteRequest;
import com.example.voting_back.dto.CreateVoteResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vote")
public class VoteController {

    private final VoteService voteService;
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/new")
    public ApiResponse<CreateVoteResponse> createVote(@RequestBody CreateVoteRequest req) {
        Long id = voteService.create(req);
        return ApiResponse.ok(new CreateVoteResponse(id));
    }

}

