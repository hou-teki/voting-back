package com.example.voting_back.dto.votelist;

import com.example.voting_back.dto.OptionWithCount;

import java.util.List;

public record VoteListItem (
        Long id,
        String title,
        String description,
        Long creatorId,
        String startDate,
        String endDate,

        boolean canViewResult,
        boolean canCast,

        List<OptionWithCount> options,
        Long total
) {}

