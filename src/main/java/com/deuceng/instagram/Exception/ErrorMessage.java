package com.deuceng.instagram.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private int statusCode;      // HTTP Hata Kodu (Örn: 409, 400)
    private LocalDateTime timestamp; // Hatanın gerçekleştiği zaman
    private String message;      // Kullanıcıya göstereceğimiz mesaj
    private String description;  // Hatanın hangi adreste (URL) olduğu
}