package com.example.voting_back.dto.response;

import com.example.voting_back.entity.enums.AgeRange;
import com.example.voting_back.entity.enums.Gender;

public record LoginResponse(
        String token,
        UserDto user
) {
    public record UserDto(
            Long id,
            String username,
            AgeRange ageRange,
            Gender gender,
            String department
    ) {
    }
}
