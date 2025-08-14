package com.example.voting_back.dto;

import java.util.List;

public record CastVoteResponse(
        Long voteId,
        long total,
        List<OptionWithCount> options
) {
}
