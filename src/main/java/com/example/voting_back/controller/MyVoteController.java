package com.example.voting_back.controller;

import com.example.voting_back.service.MyVoteService;
import com.example.voting_back.common.ApiResponse;
import com.example.voting_back.dto.VoteItemDto;
import com.example.voting_back.dto.votelist.VoteListItem;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class MyVoteController {

    private final MyVoteService myVoteService;

    @GetMapping("/created")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<VoteItemDto>> myCreated(@RequestParam Long userId) {
        List<VoteItemDto> list = myVoteService.listMyCreated(userId);
        return ApiResponse.ok(list);
    }

    @GetMapping("/participated")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<VoteListItem>> myParticipated(@RequestParam Long userId) {
        List<VoteListItem> list = myVoteService.listMyParticipated(userId);
        return ApiResponse.ok(list);
    }
}
