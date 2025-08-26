package com.example.voting_back.repository;

import com.example.voting_back.entity.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    // +options preloaded to avoid N+1
    @Deprecated
    @EntityGraph(attributePaths = "options")
    List<Vote> findAll();

    @EntityGraph(attributePaths = "options")
    Page<Vote> findAll(Pageable pageable);

    List<Vote> findByCreatorIdOrderByIdDesc(Long creatorId);
}
