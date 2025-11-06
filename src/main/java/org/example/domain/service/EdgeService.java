package org.example.domain.service;

import org.example.domain.model.ConnectsTo;
import org.example.domain.model.Guards;
import org.example.domain.model.LocatedIn;
import org.example.domain.model.StoredIn;

import java.util.List;

public interface EdgeService {
    ConnectsTo connectRooms(String fromRoomId, String toRoomId, ConnectsTo connection);
    LocatedIn placeMonsterInRoom(String monsterId, String roomId, LocatedIn location);
    StoredIn storeTreasureInRoom(String treasureId, String roomId, StoredIn storage);
    Guards monsterGuardsTreasure(String monsterId, String treasureId, Guards guard);
    
    void deleteEdge(String edgeId);
    List<ConnectsTo> getRoomConnections(String roomId);
}