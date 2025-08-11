package com.example.voting_back.Service;

import com.example.voting_back.entity.User;
import com.example.voting_back.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User register(String username, String password) {
        if (repo.existsByUsername(username)) {
            throw new IllegalArgumentException("USERNAME_TAKEN");
        }
        User user = User.builder()
                .username(username)
                .password(password)
                .build();
        return repo.save(user);
    }
}
