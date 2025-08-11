package com.example.voting_back.Service;

import com.example.voting_back.dto.CreateVoteRequest;
import com.example.voting_back.entity.OptionItem;
import com.example.voting_back.entity.Vote;
import com.example.voting_back.repository.VoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.beans.Transient;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class VoteService {

    private final VoteRepository repo;
    public VoteService(VoteRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Long create(CreateVoteRequest req) {
        // CHECK
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

        LocalDate start = LocalDate.parse(req.startDate(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDate end = req.endDate() == null || req.endDate().isEmpty()
                ? LocalDate.parse("9999-12-31")
                : LocalDate.parse(req.endDate(), DateTimeFormatter.ISO_DATE_TIME);
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("end is earlier than start");
        }

        Vote vote = Vote.builder()
                .title(req.title().trim())
                .description(req.description())
                .creatorId(req.creatorId())
                .startDate(start)
                .endDate(end)
                .options(new ArrayList<>())
                .build();

        boolean hasAtLeast2 = req.options() != null && req.options().stream()
                .filter(o -> o != null && o.label() != null && !o.label().trim().isEmpty())
                .limit(2).count() == 2;
        if (!hasAtLeast2) {
            throw new IllegalArgumentException("should have at least 2 options");
        }

        req.options().forEach(o -> {
            if (o != null && o.label() != null && !o.label().trim().isEmpty()) {
                OptionItem optionItem = new OptionItem();
                optionItem.setLabel(o.label().trim());
                vote.addOption(optionItem);
            }
        });

        return repo.save(vote).getId();
    }
}
