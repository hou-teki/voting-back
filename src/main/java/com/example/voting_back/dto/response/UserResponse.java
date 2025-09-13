package com.example.voting_back.dto.response;

import com.example.voting_back.entity.enums.AgeRange;
import com.example.voting_back.entity.enums.Gender;

public record UserResponse(
        String token,
        UserProfileDto user
) {
    public record UserProfileDto(
            Long id,
            String username,
            AgeRange ageRange,
            Gender gender,
            String department
    ) {
    }
}
