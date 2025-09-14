package com.example.voting_back.service;

import com.example.voting_back.dto.response.UserResponse;
import com.example.voting_back.entity.User;
import com.example.voting_back.entity.UserProfile;
import com.example.voting_back.repository.UserProfileRepository;
import com.example.voting_back.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;
    private AuthService authService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userProfileRepository = mock(UserProfileRepository.class);
        authService = new AuthService(userRepository, userProfileRepository);
    }

    @Test
    void register_fails_when_username_taken() {
        when(userRepository.existsByUsername("bob")).thenReturn(true);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.register("bob", "pass"));
        assertTrue(ex.getMessage().contains("Username taken"));
    }

    @Test
    void register_success_creates_user_and_profile() {
        when(userRepository.existsByUsername("bob")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(100L);
            return u;
        });
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse.UserProfileDto dto = authService.register("bob", "pass");
        assertEquals(100L, dto.id());
        assertEquals("bob", dto.username());
        assertNull(dto.ageRange());
        assertNull(dto.gender());
        assertNull(dto.department());
    }

    @Test
    void login_fails_when_user_not_found() {
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.login("bob", "pass"));
        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void login_fails_when_password_wrong() {
        User u = new User();
        u.setId(2L);
        u.setUsername("alice");
        // Use a bcrypt hash for "correct"
        User temp = new User();
        // Create a temporary service instance to obtain encoder behavior
        // But AuthService uses internal encoder, so we just precompute a hash externally
        String hash = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("correct");
        u.setPassword(hash);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(u));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.login("alice", "wrong"));
        assertTrue(ex.getMessage().contains("Invalid credentials"));
    }
}

