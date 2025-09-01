package com.example.voting_back.dto.user;

public record LoginRequest(
        String username,
        String password
) {
}
