package org.example.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ConnectRoomsRequest {
    @NotBlank(message = "From room ID is required")
    private String fromRoomId;
    
    @NotBlank(message = "To room ID is required")
    private String toRoomId;
    
    @Pattern(regexp = "north|south|east|west|northeast|northwest|southeast|southwest|up|down",
             message = "Direction must be a valid compass direction")
    private String direction;
    
    @Pattern(regexp = "open|locked|secret|trapped|magical",
             message = "Door type must be one of: open, locked, secret, trapped, magical")
    private String doorType;
    
    @Min(value = 0, message = "Distance cannot be negative")
    private Integer distance;
    
    // Getters and Setters
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