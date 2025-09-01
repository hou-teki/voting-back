package com.example.voting_back.dto.user;

public record LoginResponse(
        String token,
        UserDto user
) {
}
