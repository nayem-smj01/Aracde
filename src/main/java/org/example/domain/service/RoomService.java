
package org.example.domain.service;

import org.example.domain.model.*;
import java.util.List;
import java.util.Optional;

public interface RoomService {
    Room createRoom(Room room);
    Optional<Room> getRoomById(String id);
    List<Room> getAllRooms();
    Room updateRoom(String id, Room room);
    void deleteRoom(String id);
    List<Room> getConnectedRooms(String roomId);
}