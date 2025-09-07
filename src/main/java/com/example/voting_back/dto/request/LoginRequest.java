package com.example.voting_back.dto.request;

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
}
