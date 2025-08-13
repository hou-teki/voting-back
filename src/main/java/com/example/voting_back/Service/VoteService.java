package com.example.voting_back.Service;

import com.example.voting_back.dto.CreateVoteRequest;
import com.example.voting_back.dto.OptionWithCount;
import com.example.voting_back.dto.VoteListItemResponse;
import com.example.voting_back.entity.OptionItem;
import com.example.voting_back.entity.Vote;
import com.example.voting_back.repository.RecordRepository;
import com.example.voting_back.repository.VoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final RecordRepository recordRepository;
    public VoteService(VoteRepository voteRepository, RecordRepository recordRepository) {
        this.voteRepository = voteRepository;
        this.recordRepository = recordRepository;
    }

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
}
