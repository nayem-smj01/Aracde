package org.example.api.dto;

import jakarta.validation.constraints.*;

public class CreateRoomRequest {
    @NotBlank(message = "Room name is required")
    private String name;
    
    private String description;
    
    @Pattern(regexp = "small|medium|large|huge", message = "Size must be: small, medium, large, or huge")
    private String size;
    
    @Pattern(regexp = "bright|dim|dark", message = "Light level must be: bright, dim, or dark")
    private String lightLevel;
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    
    public String getLightLevel() { return lightLevel; }
    public void setLightLevel(String lightLevel) { this.lightLevel = lightLevel; }
}
