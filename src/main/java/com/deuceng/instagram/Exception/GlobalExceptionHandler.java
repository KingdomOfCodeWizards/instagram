package com.deuceng.instagram.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

@ControllerAdvice // Bu anotasyon projedeki tüm hataları bu sınıfın izlemesini sağlar
public class GlobalExceptionHandler {

    // 1. UserAlreadyExistsException hatası fırlatılırsa burası çalışır
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleUserAlreadyExists(UserAlreadyExistsException ex, WebRequest request) {

        ErrorMessage message = new ErrorMessage(
                HttpStatus.CONFLICT.value(), // 409 Conflict kodu
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    // 2. Şifrelerin uyuşmaması gibi durumlarda fırlattığın IllegalArgumentException'ı yakalar
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {

        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(), // 400 Bad Request kodu
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    // 3. Sistemdeki diğer tüm beklenmedik hataları (NullPointer vb.) yakalar
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {

        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500 Sunucu Hatası
                LocalDateTime.now(),
                "Sistem hatası: " + ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}