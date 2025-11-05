package org.example.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatabaseInfo {
    
    private final String name;
    private final LocalDateTime timestamp;
    
    public DatabaseInfo(String name) {
        this.name = name;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getName() {
        return name;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}