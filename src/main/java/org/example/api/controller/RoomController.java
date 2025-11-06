
package org.example.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.api.dto.CreateRoomRequest;
import org.example.domain.model.Room;
import org.example.domain.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@Tag(name = "Room Management", description = "APIs for managing dungeon rooms")
public class RoomController {
    
    private final RoomService roomService;
    
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new room")
    public ResponseEntity<Room> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        Room room = new Room(
            request.getName(),
            request.getDescription(),
            request.getSize(),
            request.getLightLevel()
        );
        Room created = roomService.createRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping
    @Operation(summary = "Get all rooms")
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get room by ID")
    public ResponseEntity<Room> getRoomById(@PathVariable String id) {
        return roomService.getRoomById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a room")
    public ResponseEntity<Room> updateRoom(
            @PathVariable String id,
            @Valid @RequestBody CreateRoomRequest request) {
        Room room = new Room(
            request.getName(),
            request.getDescription(),
            request.getSize(),
            request.getLightLevel()
        );
        Room updated = roomService.updateRoom(id, room);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a room")
    public ResponseEntity<Void> deleteRoom(@PathVariable String id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/connected")
    @Operation(summary = "Get rooms connected to this room")
    public ResponseEntity<List<Room>> getConnectedRooms(@PathVariable String id) {
        return ResponseEntity.ok(roomService.getConnectedRooms(id));
    }
}
