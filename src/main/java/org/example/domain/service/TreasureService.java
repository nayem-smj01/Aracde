package org.example.domain.service;

import org.example.domain.model.Treasure;

import java.util.List;
import java.util.Optional;

public interface TreasureService {
    Treasure createTreasure(Treasure treasure);
    Optional<Treasure> getTreasureById(String id);
    List<Treasure> getAllTreasures();
    Treasure updateTreasure(String id, Treasure treasure);
    void deleteTreasure(String id);
    List<Treasure> getTreasuresInRoom(String roomId);
}