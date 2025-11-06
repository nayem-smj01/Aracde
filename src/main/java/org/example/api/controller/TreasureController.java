package org.example.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.api.dto.CreateTreasureRequest;
import org.example.domain.model.Treasure;
import org.example.domain.service.TreasureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/treasures")
@Tag(name = "Treasure Management", description = "APIs for managing treasures")
public class TreasureController {
    
    private final TreasureService treasureService;
    
    public TreasureController(TreasureService treasureService) {
        this.treasureService = treasureService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new treasure")
    public ResponseEntity<Treasure> createTreasure(@Valid @RequestBody CreateTreasureRequest request) {
        Treasure treasure = new Treasure(
            request.getName(),
            request.getGp(),
            request.getType(),
            request.getRarity(),
            request.getDescription()
        );
        Treasure created = treasureService.createTreasure(treasure);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping
    @Operation(summary = "Get all treasures")
    public ResponseEntity<List<Treasure>> getAllTreasures() {
        return ResponseEntity.ok(treasureService.getAllTreasures());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get treasure by ID")
    public ResponseEntity<Treasure> getTreasureById(@PathVariable String id) {
        return treasureService.getTreasureById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a treasure")
    public ResponseEntity<Treasure> updateTreasure(
            @PathVariable String id,
            @Valid @RequestBody CreateTreasureRequest request) {
        Treasure treasure = new Treasure(
            request.getName(),
            request.getGp(),
            request.getType(),
            request.getRarity(),
            request.getDescription()
        );
        Treasure updated = treasureService.updateTreasure(id, treasure);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a treasure")
    public ResponseEntity<Void> deleteTreasure(@PathVariable String id) {
        treasureService.deleteTreasure(id);
        return ResponseEntity.noContent().build();
    }
}