package com.deuceng.instagram.DTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotBlank(message = "Kullanıcı adı veya email boş olamaz")
   private String identifier;
    @NotBlank(message = "Şifre boş olamaz")
   private String password;
}
