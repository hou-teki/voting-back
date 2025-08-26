package com.example.voting_back.dto.votelist;

import java.util.List;

public record VotePage<T>(
        List<T> items,
        int page,
        int size,
        boolean hasNext
) {}

