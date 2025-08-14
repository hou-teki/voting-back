package com.example.voting_back.repository;

import com.example.voting_back.entity.OptionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<OptionItem, Long> {

    List<OptionItem> findByVoteIdOrderByIdAsc(Long voteId);
}
