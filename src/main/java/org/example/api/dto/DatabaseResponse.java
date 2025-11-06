package org.example.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatabaseResponse {
    
    private final List<DatabaseDto> databases;
    private final int count;
    private final LocalDateTime timestamp;
    
    public DatabaseResponse(List<DatabaseDto> databases) {
        this.databases = databases;
        this.count = databases.size();
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public List<DatabaseDto> getDatabases() { return databases; }
    public int getCount() { return count; }
    public LocalDateTime getTimestamp() { return timestamp; }
}