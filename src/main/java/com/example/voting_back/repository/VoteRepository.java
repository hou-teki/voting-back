package com.example.voting_back.repository;

import com.example.voting_back.entity.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    // +options preloaded to avoid N+1
    @EntityGraph(attributePaths = "options")
    List<Vote> findAll();

    List<Vote> findByCreatorIdOrderByIdDesc(Long creatorId);
}
