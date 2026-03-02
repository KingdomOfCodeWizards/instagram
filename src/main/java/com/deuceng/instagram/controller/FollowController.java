package com.deuceng.instagram.controller;


import com.deuceng.instagram.dto.UserSummaryDto;
import com.deuceng.instagram.service.IFollowService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Builder
@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final IFollowService followService;

    @PostMapping("/{targetUserId}") //takip etme
    public ResponseEntity<Void>follow(@PathVariable Long targetUserId){
        followService.follow(targetUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetUserId}") //takipten çıkma
    public ResponseEntity<Void>unfollow(@PathVariable Long targetUserId){
        followService.unfollow(targetUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserSummaryDto>>followers(@PathVariable Long userId){
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserSummaryDto>>following(@PathVariable Long userId){
        return ResponseEntity.ok(followService.getFollowing(userId));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<UserSummaryDto>>pendingRequests(){
        return ResponseEntity.ok(followService.getPendingRequests());
    }

    @PostMapping("/requests/{requesterUserId}/accept")
    public ResponseEntity<Void> accept(@PathVariable Long requesterUserId) {
        followService.acceptRequest(requesterUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/requests/{requesterUserId}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long requesterUserId) {
        followService.rejectRequest(requesterUserId);
        return ResponseEntity.ok().build();
    }



}
