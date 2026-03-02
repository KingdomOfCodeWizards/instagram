package com.deuceng.instagram.service.impl;

import com.deuceng.instagram.dto.UserSummaryDto;
import com.deuceng.instagram.entity.Follow;
import com.deuceng.instagram.entity.User;
import com.deuceng.instagram.enums.FollowStatus;
import com.deuceng.instagram.repository.FollowRepository;
import com.deuceng.instagram.repository.UserRepository;
import com.deuceng.instagram.service.IFollowService;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Data
@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements IFollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void follow(Long targetUserId) {

        User me = getCurrentUser();
        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Takip edilecek kullanıcı bulunamadı"));

        // kendini takip etme
        if (me.getId().equals(target.getId())) {
            throw new RuntimeException("Kendini takip edemezsin");
        }

        // zaten takip ediyorsa
        if (followRepository.existsByFollowerAndFollowing(me, target)) {
            return;
        }

        FollowStatus status = target.isPrivate() ? FollowStatus.PENDING : FollowStatus.ACCEPTED;
        Follow follow = Follow.builder()
                .follower(me)
                .following(target)
                .status(status)
                .build();

        followRepository.save(follow);
    }


    @Override
    @Transactional
    public void unfollow(Long targetUserId) {

        User me = getCurrentUser();
        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        followRepository.deleteByFollowerAndFollowing(me, target);
    }

    @Override
    public List<UserSummaryDto> getFollowers(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // useri takip edenler
        return followRepository.findAllByFollowingAndStatus(user,FollowStatus.ACCEPTED).stream()
                .map(f -> toUserSummary(f.getFollower()))
                .toList();
    }



    @Override
    public List<UserSummaryDto> getFollowing(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        //userin takip ettikleri
        return followRepository.findAllByFollowerAndStatus(user , FollowStatus.ACCEPTED).stream()
                .map(f -> toUserSummary(f.getFollowing()))
                .toList();
    }



    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Yetkisiz erişim");
        }

        String username = auth.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    private UserSummaryDto toUserSummary(User user) {
        return UserSummaryDto.builder()
                .id((long) user.getId()) // sende int ise cast; Long ise direkt user.getId()
                .username(user.getUsername())
                .profileUrl(user.getProfile_url())
                .build();
    }

    @Override
    public List<UserSummaryDto> getPendingRequests() {
        User me = getCurrentUser();

        return followRepository.findAllByFollowingAndStatus(me, FollowStatus.PENDING).stream()
                .map(f -> toUserSummary(f.getFollower()))
                .toList();
    }

    @Override
    @Transactional
    public void acceptRequest(Long requesterUserId) {
        User me = getCurrentUser(); // private hesap sahibi
        User requester = userRepository.findById(requesterUserId)
                .orElseThrow(() -> new RuntimeException("Request atan kullanıcı bulunamadı"));

        Follow follow = followRepository.findByFollowerAndFollowing(requester, me)
                .orElseThrow(() -> new RuntimeException("Follow request bulunamadı"));

        if (follow.getStatus() != FollowStatus.PENDING) {
            throw new RuntimeException("Bu istek pending değil");
        }

        follow.setStatus(FollowStatus.ACCEPTED);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void rejectRequest(Long requesterUserId) {
        User me = getCurrentUser();

        long deleted = followRepository.deleteByFollowerIdAndFollowingIdAndStatus(
                requesterUserId,
                me.getId(),
                FollowStatus.PENDING
        );

        if (deleted == 0) {
            throw new RuntimeException("Pending request bulunamadı (ya yok, ya zaten ACCEPTED)");
        }
    }

}




