package com.example.voting_back.dto.response;

import java.util.List;

public record VoteResponse(
        Long id,
        String title,
        String description,
        Long creatorId,
        String startDate,
        String endDate,
        List<VoteOptionResponse> options,

        Long totalVotes,
        Boolean canViewResult,
        Boolean canCast
) {

    public record VoteOptionResponse(
            Long id,
            String label,
            Long count
    ) {
    }
}
