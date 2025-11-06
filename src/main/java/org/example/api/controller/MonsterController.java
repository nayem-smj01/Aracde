package org.example.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.api.dto.CreateMonsterRequest;
import org.example.domain.model.Monster;
import org.example.domain.service.MonsterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/monsters")
@Tag(name = "Monster Management", description = "APIs for managing monsters")
public class MonsterController {
    
    private final MonsterService monsterService;
    
    public MonsterController(MonsterService monsterService) {
        this.monsterService = monsterService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new monster")
    public ResponseEntity<Monster> createMonster(@Valid @RequestBody CreateMonsterRequest request) {
        Monster monster = new Monster(
            request.getName(),
            request.getCr(),
            request.getXp(),
            request.getHp(),
            request.getAc(),
            request.getType()
        );
        Monster created = monsterService.createMonster(monster);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping
    @Operation(summary = "Get all monsters")
    public ResponseEntity<List<Monster>> getAllMonsters() {
        return ResponseEntity.ok(monsterService.getAllMonsters());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get monster by ID")
    public ResponseEntity<Monster> getMonsterById(@PathVariable String id) {
        return monsterService.getMonsterById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a monster")
    public ResponseEntity<Monster> updateMonster(
            @PathVariable String id,
            @Valid @RequestBody CreateMonsterRequest request) {
        Monster monster = new Monster(
            request.getName(),
            request.getCr(),
            request.getXp(),
            request.getHp(),
            request.getAc(),
            request.getType()
        );
        Monster updated = monsterService.updateMonster(id, monster);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a monster")
    public ResponseEntity<Void> deleteMonster(@PathVariable String id) {
        monsterService.deleteMonster(id);
        return ResponseEntity.noContent().build();
    }
}