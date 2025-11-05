package org.example.infrastructure.service;

public class DatabaseAlreadyExistsException extends RuntimeException {
    
    public DatabaseAlreadyExistsException(String message) {
        super(message);
    }
}