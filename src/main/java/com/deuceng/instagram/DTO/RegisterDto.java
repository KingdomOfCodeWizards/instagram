package com.deuceng.instagram.DTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String name;
    private String surname;
    @NotBlank(message = " Kullanıcı adı giriniz!")
    @Size(min = 6, message = "Kullanıcı adı en az 4 karakter olmalı!")
    private String username;
    @NotBlank(message = "Lütfen şifre oluşturunuz!")
    @Size(message = "Şifre en az 4 karakterli olmalıdır!")
    private String password;
    @Email
    private String email;
    @NotBlank(message = "Şifreyi tekrar giriniz!")
    private String confirmPassword;
}
