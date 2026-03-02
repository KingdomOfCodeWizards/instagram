package com.deuceng.instagram.service.impl;

import com.deuceng.instagram.dto.LoginDto;
import com.deuceng.instagram.dto.RegisterDto;
import com.deuceng.instagram.dto.UserResponseDto;
import com.deuceng.instagram.entity.User;
import com.deuceng.instagram.exception.UserAlreadyExistsException;
import com.deuceng.instagram.repository.UserRepository;
import com.deuceng.instagram.security.JwtService;
import com.deuceng.instagram.service.IAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto login(LoginDto loginDto) {

        User user = userRepository
                .findByUsernameOrEmail(
                        loginDto.getIdentifier(),
                        loginDto.getIdentifier()
                )
                .orElseThrow(() ->
                        new BadCredentialsException("Geçersiz kullanıcı adı veya şifre")
                );

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Geçersiz kullanıcı adı veya şifre");
        }

        String jwtToken = jwtService.generateToken(user.getUsername());

        return new UserResponseDto(
                jwtToken,
                user.getUsername(),
                user.getBio(),
                user.isPrivate(),
                user.getProfile_url()
        );
    }

    @Override
    public UserResponseDto register(RegisterDto registerDto) {

        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Girilen şifreler farklı!");
        }

        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Girdiğiniz kullanıcı adı alınmış!");
        }

        User newUser = new User();
        newUser.setUsername(registerDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        newUser.setEmail(registerDto.getEmail());
        newUser.setPrivate(registerDto.isPrivate());

        userRepository.save(newUser);

        String jwtToken = jwtService.generateToken(newUser.getUsername());

        return new UserResponseDto(
                jwtToken,
                newUser.getUsername(),
                newUser.getBio(),
                newUser.isPrivate(),
                newUser.getProfile_url()
        );
    }
}