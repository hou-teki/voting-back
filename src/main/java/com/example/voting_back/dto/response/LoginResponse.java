package com.example.voting_back.dto.response;

public record LoginResponse(
        String token,
        UserDto user
) {
    public record UserDto(
            Long id,
            String username
    ) {
    }
}
