package com.example.voting_back.repository;

import com.example.voting_back.entity.OptionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<OptionItem, Long> {
}
