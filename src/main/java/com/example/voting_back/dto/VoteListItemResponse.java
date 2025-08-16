package com.example.voting_back.dto;

import java.util.List;

public record VoteListItemResponse(
        Long id,
        String title,
        String description,
        Long creatorId,
        String startDate,
        String endDate,
        List<OptionWithCount> options,
        Long total
) {}

