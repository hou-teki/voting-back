package com.example.voting_back.dto;

public record CastVoteRequest(
        Long userId,
        Long voteId,
        Long optionId
) {
}
