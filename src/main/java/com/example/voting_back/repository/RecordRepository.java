package com.example.voting_back.repository;

import com.example.voting_back.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface RecordRepository extends JpaRepository<Record, Long> {

    interface OptionCount {
        Long getOptionId();
        long getCnt();
    }

    interface VoteCount {
        Long getVoteId();
        long getCnt();
    }

    @Query("""
            select r.option.id as optionId, count(r.id) as cnt
            from Record r
            group by r.option.id
            """)
    List<OptionCount> countGroupByOptionId();

    @Query("""
            select r.vote.id as voteId, count(r.id) as cnt
            from Record r
            group by r.vote.id
            """)
    List<VoteCount> countGroupByVoteId();

    @Query("""
            select r.option.id as optionId, count(r.id) as cnt
            from Record r
            where r.vote.id = :voteId
            group by r.option.id
            """)
    List<OptionCount> countOptionsByVoteId(@Param("voteId") Long voteId);

    boolean existsByUserIdAndVoteId(Long userId, Long voteId);

    @Query("""
            select r.vote.id
            from Record r
            where r.userId = :userId
            """)
    Set<Long> findVoteIdByUserId(@Param("userId") Long userId);
}
