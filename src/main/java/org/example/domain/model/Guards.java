package org.example.domain.model;

public class Guards {
    private String id;
    private String monsterId;
    private String treasureId;
    private String awareness;
    private Integer priority;
    
    public Guards() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getMonsterId() { return monsterId; }
    public void setMonsterId(String monsterId) { this.monsterId = monsterId; }
    
    public String getTreasureId() { return treasureId; }
    public void setTreasureId(String treasureId) { this.treasureId = treasureId; }
    
    public String getAwareness() { return awareness; }
    public void setAwareness(String awareness) { this.awareness = awareness; }
    
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
}