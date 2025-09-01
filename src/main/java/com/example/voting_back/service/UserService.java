package com.example.voting_back.service;

import com.example.voting_back.dto.user.UserDto;
import com.example.voting_back.entity.User;
import com.example.voting_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserDto register(String username, String password) {
        // 1. check
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new RuntimeException("INVALID_INPUT");
        }

        // 2. encode password
        String trimUsername = username.trim();
        String encodedPassword = encoder.encode(password);

        // 3. insert user
        if (repo.existsByUsername(trimUsername)) {
            throw new IllegalArgumentException("USERNAME_TAKEN");
        }
        User user = User.builder()
                .username(trimUsername)
                .password(encodedPassword)
                .build();
        User registeredUser = repo.save(user);

        return new UserDto(registeredUser.getId(), registeredUser.getUsername());
    }

    public UserDto login(String username, String password) {
        User user = repo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        boolean matches = encoder.matches(password, user.getPassword());
        if (!matches) {
            throw new RuntimeException("Invalid credentials");
        }
        return new UserDto(user.getId(), user.getUsername());
    }
}
