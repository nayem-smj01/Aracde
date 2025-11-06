package org.example.domain.model;

public class Room {
    private String id;
    private String name;
    private String description;
    private String size;
    private String lightLevel;
    
    // Constructors
    public Room() {}
    
    public Room(String name, String description, String size, String lightLevel) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.lightLevel = lightLevel;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    
    public String getLightLevel() { return lightLevel; }
    public void setLightLevel(String lightLevel) { this.lightLevel = lightLevel; }
}
