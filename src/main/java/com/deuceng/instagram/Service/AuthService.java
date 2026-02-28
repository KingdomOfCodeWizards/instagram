package com.deuceng.instagram.Service;
import com.deuceng.instagram.DTO.LoginDto;
import com.deuceng.instagram.DTO.RegisterDto;
import com.deuceng.instagram.DTO.UserResponseDto;
import com.deuceng.instagram.Entity.User;
import com.deuceng.instagram.Exception.UserAlreadyExistsException;
import com.deuceng.instagram.Repository.UserRepository;
import com.deuceng.instagram.Security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto login(LoginDto loginDto) {
        User user = userRepository.findByUsernameOrEmail(loginDto.getIdentifier(),loginDto.getIdentifier())
                .orElseThrow(() -> new BadCredentialsException("Geçersiz kullanıcı adı veya şifre"));
        if(!passwordEncoder.matches(loginDto.getPassword(),user.getPassword())){
            throw new BadCredentialsException("Geçersiz kullanıcı adı veya şifre");
        }

        String jwtToken = jwtService.generateToken(user.getUsername());

        return new UserResponseDto(jwtToken,user.getUsername(),
                user.getBio(),
                user.isPrivate(),
                user.getProfile_url());
    }

    public UserResponseDto register(RegisterDto registerDto) {
        if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Girilen şifreler farklı!");
        }
        if(userRepository.findByUsername(registerDto.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("Girdiğiniz kullancı adı alınmış!");
        }
        User newUser = new User();
        newUser.setUsername(registerDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        newUser.setEmail(registerDto.getEmail());

        userRepository.save(newUser);
        String jwtToken = jwtService.generateToken(registerDto.getUsername());

        return new UserResponseDto(jwtToken,
                newUser.getUsername(),
                newUser.getBio(),
                newUser.isPrivate(),
                newUser.getProfile_url());
    }
}
