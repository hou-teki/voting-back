package com.example.voting_back.dto;

import java.util.List;

public record VoteListItemResponse(
        String title,
        String description,
        Long creatorId,
        String startDate,
        String endDate,
        List<OptionWithCount> options
) {}

