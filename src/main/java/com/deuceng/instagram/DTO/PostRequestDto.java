package com.deuceng.instagram.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostRequestDto {
    @NotBlank(message = "Lütfen bir fotoğraf seçiniz!")
    private String photoUrl;
    private String caption;
}
