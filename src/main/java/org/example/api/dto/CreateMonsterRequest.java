package org.example.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateMonsterRequest {
    @NotBlank(message = "Monster name is required")
    private String name;
    
    @Min(value = 0, message = "CR must be 0 or greater")
    @Max(value = 30, message = "CR must be 30 or less")
    private Integer cr;
    
    @Min(value = 0, message = "XP cannot be negative")
    private Integer xp;
    
    @Min(value = 1, message = "HP must be at least 1")
    private Integer hp;
    
    @Min(value = 1, message = "AC must be at least 1")
    private Integer ac;
    
    private String type;
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getCr() { return cr; }
    public void setCr(Integer cr) { this.cr = cr; }
    
    public Integer getXp() { return xp; }
    public void setXp(Integer xp) { this.xp = xp; }
    
    public Integer getHp() { return hp; }
    public void setHp(Integer hp) { this.hp = hp; }
    
    public Integer getAc() { return ac; }
    public void setAc(Integer ac) { this.ac = ac; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
