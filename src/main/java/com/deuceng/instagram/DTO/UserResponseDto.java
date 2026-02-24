package com.deuceng.instagram.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String username;
    private String bio;
    private boolean isPrivate;
    private String profile_url;
}
