package com.example.voting_back.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        int code,
        String message,
        T data
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "OK", data);
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(500, message);
    }
}
