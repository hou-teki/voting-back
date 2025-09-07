package com.example.voting_back.service;

import com.example.voting_back.dto.request.VoteRequest;
import com.example.voting_back.dto.response.VoteResponse;
import com.example.voting_back.entity.OptionItem;
import com.example.voting_back.entity.Vote;
import com.example.voting_back.entity.Record;
import com.example.voting_back.repository.OptionRepository;
import com.example.voting_back.repository.RecordRepository;
import com.example.voting_back.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final OptionRepository optionRepository;
    private final RecordRepository recordRepository;

    @Transactional
    public Long create(VoteRequest req, Long creatorId) {
        // 1. single-check
        if (req == null) throw new IllegalArgumentException("req is Empty");
        if (req.title() == null || req.title().trim().isEmpty()) {
            throw new IllegalArgumentException("title is Empty");
        }
        if (creatorId == null) {
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
                .filter(label -> label != null && !label.trim().isEmpty())
                .limit(2).count() == 2;
        if (!hasAtLeast2) {
            throw new IllegalArgumentException("should have at least 2 options");
        }

        // 3. build entity
        Vote vote = Vote.builder()
                .title(req.title().trim())
                .description(req.description())
                .creatorId(creatorId)
                .startDate(start)
                .endDate(end)
                .options(new ArrayList<>())
                .build();

        req.options().forEach(label -> {
            if (label != null && !label.trim().isEmpty()) {
                OptionItem optionItem = new OptionItem();
                optionItem.setLabel(label.trim());
                vote.addOption(optionItem);
            }
        });

        return voteRepository.save(vote).getId();
    }

    public Page<VoteResponse> listAll(int page, int size, Long userId) {
        // 1. Load votes with options
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Vote> pageData = voteRepository.findAll(pageable);

        // 2. precompute counts
        // optionId : count
        Map<Long, Long> countMapByOption = recordRepository.countGroupByOptionId()
                .stream().collect(Collectors.toMap(
                        RecordRepository.OptionCount::getOptionId,
                        RecordRepository.OptionCount::getCnt
                ));

        // voteId : count
        Map<Long, Long> countMapByVote = recordRepository.countGroupByVoteId()
                .stream().collect(Collectors.toMap(
                        RecordRepository.VoteCount::getVoteId,
                        RecordRepository.VoteCount::getCnt
                ));

        // 3. build DTO
        Page<VoteResponse> res = pageData.map(v -> new VoteResponse(
                v.getId(),
                v.getTitle(),
                v.getDescription(),
                v.getCreatorId(),
                v.getStartDate() != null ? DateTimeFormatter.ISO_DATE.format(v.getStartDate()) : null,
                v.getEndDate() != null ? DateTimeFormatter.ISO_DATE.format(v.getEndDate()) : null,
                v.getOptions().stream()
                        .map(opt -> new VoteResponse.VoteOptionResponse(opt.getId(), opt.getLabel(), null))
                        .toList(),
                null,
                false,
                false
        ));

        // 3-1. unauthenticated
        if (userId == null) return res;

        // 3-2. authenticated
        Set<Long> myParticipatedVotes = recordRepository.findVoteIdByUserId(userId);
        String today = DateTimeFormatter.ISO_DATE.format(LocalDate.now());

        Page<VoteResponse> updated = res.map(v -> {
            boolean hasVoted = myParticipatedVotes.contains(v.id());

            boolean started = v.startDate() == null || v.startDate().compareTo(today) <= 0;
            boolean ended = v.endDate() != null && v.endDate().compareTo(today) < 0;

            boolean canViewResult = ended || hasVoted;
            boolean canCast = started && !ended && !hasVoted;

            List<VoteResponse.VoteOptionResponse> options = canViewResult
                    ? v.options().stream()
                    .map(opt -> new VoteResponse.VoteOptionResponse(
                            opt.id(),
                            opt.label(),
                            countMapByOption.getOrDefault(opt.id(), 0L)
                    )).toList()
                    : v.options();

            Long total = canViewResult ? countMapByVote.getOrDefault(v.id(), 0L) : null;

            return new VoteResponse(
                    v.id(), v.title(), v.description(), v.creatorId(),
                    v.startDate(), v.endDate(),
                    options, total,
                    canViewResult, canCast
            );
        });
        
        return updated;
    }

    private VoteResponse.VoteOptionResponse toOptionDto(OptionItem opt, Map<Long, Long> countMap) {
        long count = countMap.getOrDefault(opt.getId(), 0L);
        return new VoteResponse.VoteOptionResponse(opt.getId(), opt.getLabel(), count);
    }

    @Transactional
    public VoteResponse castVote(Long userId, Long voteId, Long optionId) {
        // 1. check
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found: " + voteId));

        LocalDate today = LocalDate.now();
        if (vote.getStartDate() != null && today.isBefore(vote.getStartDate())) {
            throw new IllegalArgumentException("Vote not started");
        }
        if (vote.getEndDate() != null && today.isAfter(vote.getEndDate())) {
            throw new IllegalArgumentException("Vote ended");
        }

        OptionItem option = optionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Option not found: " + optionId));
        if (!option.getVote().getId().equals(voteId)) {
            throw new IllegalArgumentException("Option doesn't belong to the vote");
        }

        if (recordRepository.existsByUserIdAndVoteId(userId, voteId)) {
            throw new IllegalArgumentException("You have already voted");
        }

        // 2. insert record
        try {
            Record record = new Record();
            record.setUserId(userId);
            record.setVote(vote);
            record.setOption(option);
            recordRepository.save(record);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Can not insert record");
        }

        // 3. return result
        Map<Long, Long> countMap = recordRepository.countOptionsByVoteId(voteId)
                .stream().collect(Collectors.toMap(RecordRepository.OptionCount::getOptionId, RecordRepository.OptionCount::getCnt));

        long total = countMap.values().stream().mapToLong(Long::longValue).sum();

        List<VoteResponse.VoteOptionResponse> items = optionRepository.findByVoteIdOrderByIdAsc(voteId)
                .stream().map(opt -> toOptionDto(opt, countMap)).toList();

        return new VoteResponse(
                voteId,
                null,
                null,
                userId,
                null,
                null,
                items,
                total,
                true,
                false
        );
    }
}
