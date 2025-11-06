package org.example.domain.model;

public class Treasure {
    private String id;
    private String name;
    private Integer gp;
    private String type;
    private String rarity;
    private String description;
    
    // Constructors
    public Treasure() {}
    
    public Treasure(String name, Integer gp, String type, String rarity, String description) {
        this.name = name;
        this.gp = gp;
        this.type = type;
        this.rarity = rarity;
        this.description = description;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getGp() { return gp; }
    public void setGp(Integer gp) { this.gp = gp; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getRarity() { return rarity; }
    public void setRarity(String rarity) { this.rarity = rarity; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}