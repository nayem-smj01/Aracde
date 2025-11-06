package org.example.domain.model;

public class ConnectsTo {
    private String id;
    private String fromRoomId;
    private String toRoomId;
    private String direction;
    private String doorType;
    private Integer distance;
    
    // Constructors, Getters, Setters...
    public ConnectsTo() {}
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getFromRoomId() { return fromRoomId; }
    public void setFromRoomId(String fromRoomId) { this.fromRoomId = fromRoomId; }
    
    public String getToRoomId() { return toRoomId; }
    public void setToRoomId(String toRoomId) { this.toRoomId = toRoomId; }
    
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
    
    public String getDoorType() { return doorType; }
    public void setDoorType(String doorType) { this.doorType = doorType; }
    
    public Integer getDistance() { return distance; }
    public void setDistance(Integer distance) { this.distance = distance; }
}