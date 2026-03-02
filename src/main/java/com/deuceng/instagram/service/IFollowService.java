package com.deuceng.instagram.service;

import com.deuceng.instagram.dto.UserSummaryDto;

import java.util.List;

public interface IFollowService {

    void follow (Long targetUserId);
    void unfollow (Long targetUserId);

    List<UserSummaryDto>getFollowers(Long userId);
    List<UserSummaryDto>getFollowing(Long userId);

    List<UserSummaryDto> getPendingRequests();
    void acceptRequest(Long requesterUserId);
    void rejectRequest(Long requesterUserId);
}
