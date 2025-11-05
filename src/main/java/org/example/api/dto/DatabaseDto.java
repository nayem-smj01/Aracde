package org.example.api.dto;

public class DatabaseDto {
    
    private String name;
    
    public DatabaseDto(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}