package com.example.voting_back.Service;

import com.example.voting_back.dto.*;
import com.example.voting_back.entity.OptionItem;
import com.example.voting_back.entity.Vote;
import com.example.voting_back.entity.Record;
import com.example.voting_back.repository.OptionRepository;
import com.example.voting_back.repository.RecordRepository;
import com.example.voting_back.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final OptionRepository optionRepository;
    private final RecordRepository recordRepository;

    @Transactional
    public Long create(CreateVoteRequest req) {
        // 1. single-check
        if (req == null) throw new IllegalArgumentException("req is Empty");
        if (req.title() == null || req.title().trim().isEmpty()) {
            throw new IllegalArgumentException("title is Empty");
        }
        if (req.creatorId() == null) {
            throw new IllegalArgumentException("creator is Empty");
        }
        if (req.startDate() == null || req.startDate().isEmpty()) {
            throw new IllegalArgumentException("start date is Empty");
        }

        // 2. cross-check
        LocalDate start = LocalDate.parse(req.startDate(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDate end = req.endDate() == null || req.endDate().isEmpty()
                ? LocalDate.parse("9999-12-31")
                : LocalDate.parse(req.endDate(), DateTimeFormatter.ISO_DATE_TIME);
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("end is earlier than start");
        }

        boolean hasAtLeast2 = req.options() != null && req.options().stream()
                .filter(o -> o != null && o.label() != null && !o.label().trim().isEmpty())
                .limit(2).count() == 2;
        if (!hasAtLeast2) {
            throw new IllegalArgumentException("should have at least 2 options");
        }

        // 3. build entity
        Vote vote = Vote.builder()
                .title(req.title().trim())
                .description(req.description())
                .creatorId(req.creatorId())
                .startDate(start)
                .endDate(end)
                .options(new ArrayList<>())
                .build();

        req.options().forEach(o -> {
            if (o != null && o.label() != null && !o.label().trim().isEmpty()) {
                OptionItem optionItem = new OptionItem();
                optionItem.setLabel(o.label().trim());
                vote.addOption(optionItem);
            }
        });

        return voteRepository.save(vote).getId();
    }

    public List<VoteListItemResponse> listAll() {
        // 1. Grab all votes with options
        List<Vote> votes = voteRepository.findAll();

        // 2. Grab all optionId with count
        Map<Long, Long> countMap = recordRepository.countAllOptions()
                .stream().collect(Collectors.toMap(RecordRepository.OptionCount::getOptionId, RecordRepository.OptionCount::getCnt));

        // 3. combine them
        return votes.stream().map(v -> new VoteListItemResponse(
                v.getTitle(),
                v.getDescription(),
                v.getCreatorId(),
                v.getStartDate() != null ? DateTimeFormatter.ISO_DATE.format(v.getStartDate()) : null,
                v.getEndDate() != null ? DateTimeFormatter.ISO_DATE.format(v.getEndDate()) : null,
                v.getOptions().stream().map(opt -> toOptionDto(opt, countMap)).toList()
        )).toList();
    }

    private OptionWithCount toOptionDto(OptionItem opt, Map<Long, Long> countMap) {
        long count = countMap.getOrDefault(opt.getId(), 0L);
        return new OptionWithCount(opt.getLabel(), count);
    }

    @Transactional
    public CastVoteResponse castVote(CastVoteRequest req) {
        // 1. check
        Vote vote = voteRepository.findById(req.voteId())
                .orElseThrow(() -> new IllegalArgumentException("Vote not found: " + req.voteId()));

        LocalDate today = LocalDate.now();
        if (vote.getStartDate() != null && today.isBefore(vote.getStartDate())) {
            throw new IllegalArgumentException("Vote not started");
        }
        if (vote.getEndDate() != null && today.isAfter(vote.getEndDate())) {
            throw new IllegalArgumentException("Vote ended");
        }

        OptionItem option = optionRepository.findById(req.optionId())
                .orElseThrow(() -> new IllegalArgumentException("Option not found: " + req.optionId()));
        if (!option.getVote().getId().equals(req.voteId())) {
            throw new IllegalArgumentException("Option doesn't belong to the vote");
        }

        if (recordRepository.existsByUserIdAndVoteId(req.userId(), req.voteId())) {
            throw new IllegalArgumentException("You have already voted");
        }

        // 2. insert record
        try {
            Record record = new Record();
            record.setUserId(req.userId());
            record.setVote(vote);
            record.setOption(option);
            recordRepository.save(record);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("You have already voted");
        }

        // 3. return result
        Map<Long, Long> countMap = recordRepository.countOptionsByVoteId(req.voteId())
                .stream().collect(Collectors.toMap(RecordRepository.OptionCount::getOptionId, RecordRepository.OptionCount::getCnt));

        long total = countMap.values().stream().mapToLong(Long::longValue).sum();

        List<OptionWithCount> items = optionRepository.findByVoteIdOrderByIdAsc(req.voteId())
                .stream().map(opt -> toOptionDto(opt, countMap)).toList();

        return new CastVoteResponse(req.voteId(), total, items);
    }
}
