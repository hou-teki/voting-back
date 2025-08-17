package com.example.voting_back.Service;

import com.example.voting_back.dto.OptionWithoutCount;
import com.example.voting_back.dto.VoteItemDto;
import com.example.voting_back.dto.VoteListItemResponse;
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

    public List<VoteItemDto> listMyCreated(Long userId) {
        // 1. Get votes created by userId
        List<Vote> votes = voteRepository.findByCreatorIdOrderByIdDesc(userId);

        // 2. form to VoteList dto
        List<VoteItemDto> res = votes.stream().map(v -> new VoteItemDto(
                v.getId(),
                v.getTitle(),
                v.getDescription(),
                v.getCreatorId(),
                v.getStartDate() != null ? DateTimeFormatter.ISO_DATE.format(v.getStartDate()) : null,
                v.getEndDate() != null ? DateTimeFormatter.ISO_DATE.format(v.getEndDate()) : null,
                v.getOptions().stream().map(opt -> new OptionWithoutCount(opt.getId(), opt.getLabel())).toList()
        )).toList();

        return res;
    }

    public List<VoteListItemResponse> listMyParticipated(Long userId) {
        List<VoteListItemResponse> list = null;
        return list;
    }
}
