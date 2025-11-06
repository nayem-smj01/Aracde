package org.example.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateTreasureRequest {
    @NotBlank(message = "Treasure name is required")
    private String name;
    
    @Min(value = 0, message = "GP cannot be negative")
    private Integer gp;
    
    @Pattern(regexp = "currency|weapon|armor|magic item|art|gem",
             message = "Type must be one of: currency, weapon, armor, magic item, art, gem")
    private String type;
    
    @Pattern(regexp = "common|uncommon|rare|very rare|legendary",
             message = "Rarity must be one of: common, uncommon, rare, very rare, legendary")
    private String rarity;
    
    private String description;
    
    // Getters and Setters
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
