package com.deuceng.instagram.service;

import com.deuceng.instagram.dto.LoginDto;
import com.deuceng.instagram.dto.RegisterDto;
import com.deuceng.instagram.dto.UserResponseDto;

public interface IAuthService {
    UserResponseDto login(LoginDto loginDto);
    UserResponseDto register(RegisterDto registerDto);
}

