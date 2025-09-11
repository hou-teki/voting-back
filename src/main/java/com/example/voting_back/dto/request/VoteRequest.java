package com.example.voting_back.dto.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record VoteRequest(

        @NotBlank(message = "Title must not be blank")
        @Size(max = 100, message = "Title must not exceed 100 characters")
        String title,

        String description,

        @NotBlank(message = "Start date must not be empty")
        String startDate,

        String endDate,

        @NotEmpty(message = "Options must not be empty")
        @Size(min = 2, max = 10, message = "Number of options must be between 2 and 10")
        List<@NotBlank(message = "Option label must not be blank") String> options
) {

}

