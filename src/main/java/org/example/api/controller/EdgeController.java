package org.example.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.api.dto.ConnectRoomsRequest;
import org.example.domain.model.ConnectsTo;
import org.example.domain.service.EdgeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/edges")
@Tag(name = "Edge Management", description = "APIs for managing relationships between entities")
public class EdgeController {
    
    private final EdgeService edgeService;
    
    public EdgeController(EdgeService edgeService) {
        this.edgeService = edgeService;
    }
    
    @PostMapping("/connect-rooms")
    @Operation(summary = "Connect two rooms")
    public ResponseEntity<ConnectsTo> connectRooms(@Valid @RequestBody ConnectRoomsRequest request) {
        ConnectsTo connection = new ConnectsTo();
        connection.setDirection(request.getDirection());
        connection.setDoorType(request.getDoorType());
        connection.setDistance(request.getDistance());
        
        ConnectsTo created = edgeService.connectRooms(
            request.getFromRoomId(),
            request.getToRoomId(),
            connection
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @DeleteMapping("/{edgeId}")
    @Operation(summary = "Delete an edge")
    public ResponseEntity<Void> deleteEdge(@PathVariable String edgeId) {
        edgeService.deleteEdge(edgeId);
        return ResponseEntity.noContent().build();
    }
}