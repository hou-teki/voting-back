package com.example.voting_back.service;

import com.example.voting_back.dto.request.UserRequest;
import com.example.voting_back.dto.response.UserResponse;
import com.example.voting_back.dto.response.VoteResponse;
import com.example.voting_back.entity.User;
import com.example.voting_back.entity.UserProfile;
import com.example.voting_back.entity.Vote;
import com.example.voting_back.repository.RecordRepository;
import com.example.voting_back.repository.UserProfileRepository;
import com.example.voting_back.repository.UserRepository;
import com.example.voting_back.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final VoteRepository voteRepository;
    private final RecordRepository recordRepository;

    public UserResponse.UserProfileDto getProfile(Long userId) {
        User user = userRepository.getReferenceById(userId);
        UserProfile profile = userProfileRepository.getReferenceById(userId);
        return toDto(user, profile);
    }

    @Transactional
    public UserResponse.UserProfileDto updateProfile(Long userId, UserRequest.UserProfileRequest updates) {
        User user = userRepository.getReferenceById(userId);
        UserProfile profile = userProfileRepository.getReferenceById(userId);

        if(updates.ageRange() != null) profile.setAgeRange(updates.ageRange());
        if(updates.gender() != null) profile.setGender(updates.gender());
        if(updates.department() != null) profile.setDepartment(updates.department());

        userProfileRepository.save(profile);

        return toDto(user, profile);
    }

    private UserResponse.UserProfileDto toDto(User user, UserProfile profile) {
        return new UserResponse.UserProfileDto(
                user.getId(),
                user.getUsername(),
                profile.getAgeRange(),
                profile.getGender(),
                profile.getDepartment()
        );
    }

    public Page<VoteResponse> listMyCreated(int page, int size, Long userId) {
        // 1. Get votes created by userId
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Vote> pageData = voteRepository.findByCreatorIdOrderByIdDesc(userId, pageable);

        // 2. form to VoteList dto
        return pageData.map(v -> new VoteResponse(
                v.getId(),
                v.getTitle(),
                v.getDescription(),
                v.getCreatorId(),
                v.getStartDate() != null ? DateTimeFormatter.ISO_DATE.format(v.getStartDate()) : null,
                v.getEndDate() != null ? DateTimeFormatter.ISO_DATE.format(v.getEndDate()) : null,
                null,
                null,
                null,
                null
        ));
    }

    public Page<VoteResponse> listMyParticipated(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Vote> pageData = voteRepository.findParticipatedByUserId(userId, pageable);

        List<Long> voteIds = pageData.getContent().stream().map(Vote::getId).toList();

        // voteId : label
        Map<Long, String> myOptionByVote = recordRepository.findMyPickedLabel(userId, voteIds).stream()
                .collect(Collectors.toMap(row -> (Long) row[0], row -> (String) row[1]));

        // build DTO
        return pageData.map(v -> new VoteResponse(
                v.getId(),
                v.getTitle(),
                v.getDescription(),
                v.getCreatorId(),
                v.getStartDate() != null ? DateTimeFormatter.ISO_DATE.format(v.getStartDate()) : null,
                v.getEndDate() != null ? DateTimeFormatter.ISO_DATE.format(v.getEndDate()) : null,
                List.of(new VoteResponse.VoteOptionResponse(null, myOptionByVote.get(v.getId()), null)),
                null,
                null,
                null
        ));
    }
}
