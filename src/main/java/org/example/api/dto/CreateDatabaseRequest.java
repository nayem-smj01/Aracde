package org.example.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateDatabaseRequest {
    
    @NotBlank(message = "Database name is required")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", 
             message = "Database name must contain only letters, numbers, underscores, and hyphens")
    private String name;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}