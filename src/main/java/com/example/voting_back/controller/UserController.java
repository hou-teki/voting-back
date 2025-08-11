package com.example.voting_back.controller;

import com.example.voting_back.Service.UserService;
import com.example.voting_back.common.ApiResponse;
import com.example.voting_back.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public record RegisterRequest(String username, String password) {}
    public record RegisterResponse(Long id, String username) {}

    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody RegisterRequest req) {
        if (req == null
                || req.username() == null || req.username().isBlank()
                || req.password() == null || req.password().isBlank()
        ) {
            return ApiResponse.error(400, "INVALID_INPUT");
        }

        String username = req.username().trim();
        String password = encoder.encode(req.password());

        try {
            User user = userService.register(username, password);
            return ApiResponse.ok(new RegisterResponse(user.getId(), user.getUsername()));
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(500, "ERROR");
        }
    }
}
