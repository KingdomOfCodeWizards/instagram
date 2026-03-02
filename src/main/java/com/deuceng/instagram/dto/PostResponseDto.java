package com.deuceng.instagram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long id;
    private String caption;
    private String imageUrl;
    private LocalDateTime createdAt;

    private UserSummaryDto user;
}
