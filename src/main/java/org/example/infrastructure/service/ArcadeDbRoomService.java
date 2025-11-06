package org.example.infrastructure.service;

import com.arcadedb.database.Document;
import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import org.example.domain.model.Room;
import org.example.domain.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArcadeDbRoomService implements RoomService {
    
    private static final Logger logger = LoggerFactory.getLogger(ArcadeDbRoomService.class);
    
    private final RemoteDatabase database;
    
    public ArcadeDbRoomService(RemoteDatabase database) {
        this.database = database;
    }
    
    @Override
    public Room createRoom(Room room) {
        logger.info("Creating room: {}", room.getName());
        
        String sql = String.format(
            "CREATE VERTEX Room SET name = '%s', description = '%s', size = '%s', lightLevel = '%s'",
            escape(room.getName()),
            escape(room.getDescription()),
            escape(room.getSize()),
            escape(room.getLightLevel())
        );
        
        try {
            ResultSet result = database.command("sql", sql);
            if (result.hasNext()) {
                Result record = result.next();
                room.setId(record.getIdentity().get().toString());
                logger.info("Room created with ID: {}", room.getId());
            }
            return room;
        } catch (Exception e) {
            logger.error("Error creating room: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to create room", e);
        }
    }
    
    @Override
    public Optional<Room> getRoomById(String id) {
        logger.debug("Fetching room by ID: {}", id);
        
        String sql = String.format("SELECT FROM %s", id);
        
        try {
            ResultSet result = database.query("sql", sql);
            if (result.hasNext()) {
                return Optional.of(mapToRoom(result.next()));
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error fetching room by ID: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to fetch room", e);
        }
    }
    
    @Override
    public List<Room> getAllRooms() {
        logger.debug("Fetching all rooms");
        
        String sql = "SELECT FROM Room";
        
        try {
            ResultSet result = database.query("sql", sql);
            List<Room> rooms = new ArrayList<>();
            
            while (result.hasNext()) {
                rooms.add(mapToRoom(result.next()));
            }
            
            logger.info("Found {} rooms", rooms.size());
            return rooms;
        } catch (Exception e) {
            logger.error("Error fetching all rooms: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to fetch rooms", e);
        }
    }
    
    @Override
    public Room updateRoom(String id, Room room) {
        logger.info("Updating room: {}", id);
        
        String sql = String.format(
            "UPDATE %s SET name = '%s', description = '%s', size = '%s', lightLevel = '%s'",
            id,
            escape(room.getName()),
            escape(room.getDescription()),
            escape(room.getSize()),
            escape(room.getLightLevel())
        );
        
        try {
            database.command("sql", sql);
            room.setId(id);
            logger.info("Room updated: {}", id);
            return room;
        } catch (Exception e) {
            logger.error("Error updating room: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to update room", e);
        }
    }
    
    @Override
    public void deleteRoom(String id) {
        logger.info("Deleting room: {}", id);
        
        String sql = String.format("DELETE VERTEX %s", id);
        
        try {
            database.command("sql", sql);
            logger.info("Room deleted: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting room: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to delete room", e);
        }
    }
    
    @Override
    public List<Room> getConnectedRooms(String roomId) {
        logger.debug("Fetching rooms connected to: {}", roomId);
        
        String sql = String.format("SELECT expand(out('ConnectsTo')) FROM %s", roomId);
        
        try {
            ResultSet result = database.query("sql", sql);
            List<Room> rooms = new ArrayList<>();
            
            while (result.hasNext()) {
                rooms.add(mapToRoom(result.next()));
            }
            
            logger.info("Found {} connected rooms", rooms.size());
            return rooms;
        } catch (Exception e) {
            logger.error("Error fetching connected rooms: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to fetch connected rooms", e);
        }
    }
    
    private Room mapToRoom(Result record) {
        Room room = new Room();
        room.setId(record.getIdentity().get().toString());
        room.setName(record.getProperty("name"));
        room.setDescription(record.getProperty("description"));
        room.setSize(record.getProperty("size"));
        room.setLightLevel(record.getProperty("lightLevel"));
        return room;
    }
    
    private String escape(String value) {
        if (value == null) return "";
        return value.replace("'", "\\'").replace("\"", "\\\"");
    }
}


