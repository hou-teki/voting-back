package com.example.voting_back.repository;

import com.example.voting_back.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface RecordRepository extends JpaRepository<Record, Long> {

    interface OptionCount {
        Long getOptionId();
        Long getCnt();
    }

    interface VoteCount {
        Long getVoteId();
        Long getCnt();
    }

    @Query("""
            select r.option.id as optionId, count(r) as cnt
            from Record r
            where r.vote.id in :voteIds
            group by r.option.id
            """)
    List<OptionCount> countGroupByOptionId(@Param("voteIds")Collection<Long> voteIds);

    @Query("""
            select r.vote.id as voteId, count(r) as cnt
            from Record r
            where r.vote.id in :voteIds
            group by r.vote.id
            """)
    List<VoteCount> countGroupByVoteId(@Param("voteIds")Collection<Long> voteIds);

    @Query("""
            select r.vote.id
            from Record r
            where r.userId = :userId
            """)
    Set<Long> findVoteIdByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndVoteId(Long userId, Long voteId);

}
