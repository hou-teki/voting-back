package com.example.voting_back.repository;

import com.example.voting_back.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    interface OptionCount {
        Long getOptionId();
        long getCnt();
    }

    @Query("""
            select r.option.id as optionId, count(r.id) as cnt
            from Record r
            group by r.option.id
            """)
    List<OptionCount> countAllOptions();
}
