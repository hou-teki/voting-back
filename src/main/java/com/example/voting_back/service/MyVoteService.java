package com.example.voting_back.service;

import com.example.voting_back.dto.response.VoteResponse;
import com.example.voting_back.entity.Vote;
import com.example.voting_back.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyVoteService {

    private final VoteRepository voteRepository;

    public List<VoteResponse> listMyCreated(Long userId) {
        // 1. Get votes created by userId
        List<Vote> votes = voteRepository.findByCreatorIdOrderByIdDesc(userId);

        // 2. form to VoteList dto
        List<VoteResponse> res = votes.stream().map(v -> new VoteResponse(
                v.getId(),
                v.getTitle(),
                v.getDescription(),
                v.getCreatorId(),
                v.getStartDate() != null ? DateTimeFormatter.ISO_DATE.format(v.getStartDate()) : null,
                v.getEndDate() != null ? DateTimeFormatter.ISO_DATE.format(v.getEndDate()) : null,
                v.getOptions().stream().map(opt -> new VoteResponse.VoteOptionResponse(opt.getId(), opt.getLabel(), null)).toList(),
                null,
                null,
                null
        )).toList();

        return res;
    }

    public List<VoteResponse> listMyParticipated(Long userId) {
        List<VoteResponse> list = null;
        return list;
    }
}
