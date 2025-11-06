package org.example.domain.model;

public class Monster {
    private String id;
    private String name;
    private Integer cr;  // Challenge Rating
    private Integer xp;  // Experience Points
    private Integer hp;  // Hit Points
    private Integer ac;  // Armor Class
    private String type;
    
    // Constructors
    public Monster() {}
    
    public Monster(String name, Integer cr, Integer xp, Integer hp, Integer ac, String type) {
        this.name = name;
        this.cr = cr;
        this.xp = xp;
        this.hp = hp;
        this.ac = ac;
        this.type = type;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
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
