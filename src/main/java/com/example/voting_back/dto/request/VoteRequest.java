package com.example.voting_back.dto.request;

import java.util.List;

public record VoteRequest(
        String title,
        String description,
        String startDate,
        String endDate,
        List<String> options
) {

}

