package com.example.voting_back.dto;

import java.util.List;

public record VoteItemDto(
        Long id,
        String title,
        String description,
        Long creatorId,
        String startDate,
        String endDate,
        List<OptionWithoutCount> options
) {
}
