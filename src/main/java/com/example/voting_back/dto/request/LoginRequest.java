package com.example.voting_back.dto.request;

import com.example.voting_back.entity.enums.AgeRange;
import com.example.voting_back.entity.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "Username must not be blank")
        @Size(min = 4, max = 50, message = "Username length must be between {min} and {max}")
        String username,

        @NotBlank(message = "Password must not be blank")
        @Size(min = 4, max = 20, message = "Password length must be between {min} and {max}")
        String password
) {
    public record UserProfileRequest(
            AgeRange ageRange,

            Gender gender,

            @Size(max = 100, message = "Department must not exceed 100 characters")
            String department
    ) {}
}
