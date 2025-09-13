package com.example.voting_back.entity;

import com.example.voting_back.entity.enums.AgeRange;
import com.example.voting_back.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user_profiles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_range")
    private AgeRange ageRange = AgeRange.UNKNOWN;

    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.UNKNOWN;

    private String department;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;
}