package com.example.voting_back.service;

import com.example.voting_back.dto.response.LoginResponse;
import com.example.voting_back.entity.User;
import com.example.voting_back.entity.UserProfile;
import com.example.voting_back.entity.enums.AgeRange;
import com.example.voting_back.entity.enums.Gender;
import com.example.voting_back.repository.UserProfileRepository;
import com.example.voting_back.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Transactional
    public LoginResponse.UserDto register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username taken");
        }

        User user = User.builder()
                .username(username)
                .password(encoder.encode(password))
                .build();
        User registeredUser = userRepository.save(user);

        // Add User Profile
        UserProfile profile = UserProfile.builder()
                .user(registeredUser)
                .ageRange(AgeRange.UNKNOWN)
                .gender(Gender.UNKNOWN)
                .department(null)
                .build();
        userProfileRepository.save(profile);

        return new LoginResponse.UserDto(
                registeredUser.getId(),
                registeredUser.getUsername(),
                null,
                null,
                null
        );
    }

    @Transactional
    public LoginResponse.UserDto login(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean matches = encoder.matches(password, user.getPassword());
        if (!matches) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return new LoginResponse.UserDto(
                user.getId(),
                user.getUsername(),
                null,
                null,
                null
        );
    }
}
