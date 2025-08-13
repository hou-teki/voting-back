package com.example.voting_back.controller;

import com.example.voting_back.Service.VoteService;
import com.example.voting_back.common.ApiResponse;
import com.example.voting_back.dto.CreateVoteRequest;
import com.example.voting_back.dto.CreateVoteResponse;
import com.example.voting_back.dto.VoteListItemResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/list")
    public List<VoteListItemResponse> list() {
        return voteService.listAll();
    }
}

