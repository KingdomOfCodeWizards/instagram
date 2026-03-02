package com.deuceng.instagram.repository;

import com.deuceng.instagram.entity.Follow;
import com.deuceng.instagram.entity.User;
import com.deuceng.instagram.enums.FollowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);

    void deleteByFollowerAndFollowing(User follower, User following);

    List<Follow> findAllByFollower(User follower); //benim takip ettiklerim
    List<Follow> findAllByFollowing(User following);//beni takip edenler

    long countByFollower(User follower); //takipçi sayısı
    long countByFollowing(User following); //takip edilen sayısı

    List<Follow> findAllByFollowingAndStatus(User following, FollowStatus status);
    List<Follow> findAllByFollowerAndStatus(User follower, FollowStatus status);

    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    long deleteByFollowerIdAndFollowingIdAndStatus(Long followerId, Long followingId, FollowStatus status);
}
