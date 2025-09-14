package com.example.voting_back.service;

import com.example.voting_back.dto.request.VoteRequest;
import com.example.voting_back.entity.Vote;
import com.example.voting_back.repository.OptionRepository;
import com.example.voting_back.repository.RecordRepository;
import com.example.voting_back.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VoteServiceTest {

    private VoteRepository voteRepository;
    private OptionRepository optionRepository;
    private RecordRepository recordRepository;
    private VoteService voteService;

    @BeforeEach
    void setup() {
        voteRepository = mock(VoteRepository.class);
        optionRepository = mock(OptionRepository.class);
        recordRepository = mock(RecordRepository.class);
        voteService = new VoteService(voteRepository, optionRepository, recordRepository);
    }

    @Test
    void create_success_returns_id_and_persists_vote_and_options() {
        VoteRequest req = new VoteRequest(
                "Title",
                "Desc",
                "2025-01-01",
                "2025-01-10",
                List.of("A", "B", "")
        );

        when(voteRepository.save(any(Vote.class))).thenAnswer(invocation -> {
            Vote v = invocation.getArgument(0);
            v.setId(99L);
            return v;
        });

        Long id = voteService.create(req, 7L);

        assertEquals(99L, id);

        ArgumentCaptor<Vote> captor = ArgumentCaptor.forClass(Vote.class);
        verify(voteRepository, times(1)).save(captor.capture());
        Vote saved = captor.getValue();
        assertEquals("Title", saved.getTitle());
        assertEquals(7L, saved.getCreatorId());
        // Only non-blank options should be added
        assertEquals(2, saved.getOptions().size());
    }

    @Test
    void create_fails_when_end_before_start() {
        VoteRequest req = new VoteRequest(
                "Title",
                null,
                "2025-01-10",
                "2025-01-01",
                List.of("A", "B")
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> voteService.create(req, 1L));
        assertTrue(ex.getMessage().contains("End is earlier than start"));
    }

    @Test
    void create_fails_when_less_than_two_options() {
        VoteRequest req = new VoteRequest(
                "Title",
                null,
                "2025-01-01",
                "2025-01-02",
                List.of("OnlyOne")
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> voteService.create(req, 1L));
        assertTrue(ex.getMessage().contains("at least 2 options"));
    }
}

