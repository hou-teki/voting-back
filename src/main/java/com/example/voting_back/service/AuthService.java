package com.example.voting_back.service;

import com.example.voting_back.dto.response.LoginResponse;
import com.example.voting_back.entity.User;
import com.example.voting_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public LoginResponse.UserDto register(String username, String password) {
        if (repo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username taken");
        }

        User user = User.builder()
                .username(username)
                .password(encoder.encode(password))
                .build();
        User registeredUser = repo.save(user);

        return new LoginResponse.UserDto(registeredUser.getId(), registeredUser.getUsername());
    }

    public LoginResponse.UserDto login(String username, String password) {
        User user = repo.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean matches = encoder.matches(password, user.getPassword());
        if (!matches) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return new LoginResponse.UserDto(user.getId(), user.getUsername());
    }
}
