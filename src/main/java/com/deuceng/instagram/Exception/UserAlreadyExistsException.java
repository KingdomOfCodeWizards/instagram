package com.deuceng.instagram.Exception;

// RuntimeException'dan türediğine emin ol!
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message); // Mesajı üst sınıfa gönderir, böylece getMessage() çalışır.
    }
}