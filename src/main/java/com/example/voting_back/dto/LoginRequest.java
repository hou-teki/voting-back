package com.example.voting_back.dto;

public record LoginRequest(
        String username,
        String password
) {
}
