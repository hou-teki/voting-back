package com.example.voting_back.controller;

import com.example.voting_back.Service.MyVoteService;
import com.example.voting_back.common.ApiResponse;
import com.example.voting_back.dto.VoteItemDto;
import com.example.voting_back.dto.VoteListItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class MyVoteController {

    private final MyVoteService myVoteService;

    @GetMapping("/created")
    public ApiResponse<List<VoteItemDto>> myCreated(@RequestParam Long userId) {
        List<VoteItemDto> list = myVoteService.listMyCreated(userId);
        return ApiResponse.ok(list);
    }

    @GetMapping("/participated")
    public ApiResponse<List<VoteListItemResponse>> myParticipated(@RequestParam Long userId) {
        List<VoteListItemResponse> list = myVoteService.listMyParticipated(userId);
        return ApiResponse.ok(list);
    }
}
