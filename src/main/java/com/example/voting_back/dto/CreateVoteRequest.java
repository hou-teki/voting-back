package com.example.voting_back.dto;

import com.example.voting_back.entity.OptionItem;

import java.util.List;

public record CreateVoteRequest(
        String title,
        String description,
        Long creatorId,
        String startDate,
        String endDate,
        List<OptionInput> options
) {
    public record OptionInput(String label) {}
}

