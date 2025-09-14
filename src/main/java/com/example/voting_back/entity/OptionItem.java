package com.example.voting_back.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "options")
@Data
public class OptionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;
}
