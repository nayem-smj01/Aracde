package org.example.domain.model;

public class StoredIn {
    private String id;
    private String treasureId;
    private String roomId;
    private Boolean hidden;
    private String containerType;
    
    public StoredIn() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTreasureId() { return treasureId; }
    public void setTreasureId(String treasureId) { this.treasureId = treasureId; }
    
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    
    public Boolean getHidden() { return hidden; }
    public void setHidden(Boolean hidden) { this.hidden = hidden; }
    
    public String getContainerType() { return containerType; }
    public void setContainerType(String containerType) { this.containerType = containerType; }
}