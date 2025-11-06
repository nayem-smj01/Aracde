package org.example.domain.service;

import org.example.domain.model.Monster;

import java.util.List;
import java.util.Optional;

public interface MonsterService {
    Monster createMonster(Monster monster);
    Optional<Monster> getMonsterById(String id);
    List<Monster> getAllMonsters();
    Monster updateMonster(String id, Monster monster);
    void deleteMonster(String id);
    List<Monster> getMonstersInRoom(String roomId);
}