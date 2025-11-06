
// ===================================
// ArcadeDbTreasureService.java
// ===================================
package org.example.infrastructure.service;

import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import org.example.domain.model.Treasure;
import org.example.domain.service.TreasureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArcadeDbTreasureService implements TreasureService {
    
    private static final Logger logger = LoggerFactory.getLogger(ArcadeDbTreasureService.class);
    
    private final RemoteDatabase database;
    
    public ArcadeDbTreasureService(RemoteDatabase database) {
        this.database = database;
    }
    
    @Override
    public Treasure createTreasure(Treasure treasure) {
        logger.info("Creating treasure: {}", treasure.getName());
        
        String sql = String.format(
            "CREATE VERTEX Treasure SET name = '%s', gp = %d, type = '%s', rarity = '%s', description = '%s'",
            escape(treasure.getName()),
            treasure.getGp(),
            escape(treasure.getType()),
            escape(treasure.getRarity()),
            escape(treasure.getDescription())
        );
        
        try {
            ResultSet result = database.command("sql", sql);
            if (result.hasNext()) {
                Result record = result.next();
                treasure.setId(record.getIdentity().get().toString());
                logger.info("Treasure created with ID: {}", treasure.getId());
            }
            return treasure;
        } catch (Exception e) {
            logger.error("Error creating treasure: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to create treasure", e);
        }
    }
    
    @Override
    public Optional<Treasure> getTreasureById(String id) {
        logger.debug("Fetching treasure by ID: {}", id);
        
        String sql = String.format("SELECT FROM %s", id);
        
        try {
            ResultSet result = database.query("sql", sql);
            if (result.hasNext()) {
                return Optional.of(mapToTreasure(result.next()));
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error fetching treasure by ID: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to fetch treasure", e);
        }
    }
    
    @Override
    public List<Treasure> getAllTreasures() {
        logger.debug("Fetching all treasures");
        
        String sql = "SELECT FROM Treasure";
        
        try {
            ResultSet result = database.query("sql", sql);
            List<Treasure> treasures = new ArrayList<>();
            
            while (result.hasNext()) {
                treasures.add(mapToTreasure(result.next()));
            }
            
            logger.info("Found {} treasures", treasures.size());
            return treasures;
        } catch (Exception e) {
            logger.error("Error fetching all treasures: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to fetch treasures", e);
        }
    }
    
    @Override
    public Treasure updateTreasure(String id, Treasure treasure) {
        logger.info("Updating treasure: {}", id);
        
        String sql = String.format(
            "UPDATE %s SET name = '%s', gp = %d, type = '%s', rarity = '%s', description = '%s'",
            id,
            escape(treasure.getName()),
            treasure.getGp(),
            escape(treasure.getType()),
            escape(treasure.getRarity()),
            escape(treasure.getDescription())
        );
        
        try {
            database.command("sql", sql);
            treasure.setId(id);
            logger.info("Treasure updated: {}", id);
            return treasure;
        } catch (Exception e) {
            logger.error("Error updating treasure: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to update treasure", e);
        }
    }
    
    @Override
    public void deleteTreasure(String id) {
        logger.info("Deleting treasure: {}", id);
        
        String sql = String.format("DELETE VERTEX %s", id);
        
        try {
            database.command("sql", sql);
            logger.info("Treasure deleted: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting treasure: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to delete treasure", e);
        }
    }
    
    @Override
    public List<Treasure> getTreasuresInRoom(String roomId) {
        logger.debug("Fetching treasures in room: {}", roomId);
        
        String sql = String.format("SELECT expand(in('StoredIn')) FROM %s", roomId);
        
        try {
            ResultSet result = database.query("sql", sql);
            List<Treasure> treasures = new ArrayList<>();
            
            while (result.hasNext()) {
                treasures.add(mapToTreasure(result.next()));
            }
            
            logger.info("Found {} treasures in room", treasures.size());
            return treasures;
        } catch (Exception e) {
            logger.error("Error fetching treasures in room: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to fetch treasures in room", e);
        }
    }
    
    private Treasure mapToTreasure(Result record) {
        Treasure treasure = new Treasure();
        treasure.setId(record.getIdentity().get().toString());
        treasure.setName(record.getProperty("name"));
        treasure.setGp(record.getProperty("gp"));
        treasure.setType(record.getProperty("type"));
        treasure.setRarity(record.getProperty("rarity"));
        treasure.setDescription(record.getProperty("description"));
        return treasure;
    }
    
    private String escape(String value) {
        if (value == null) return "";
        return value.replace("'", "\\'").replace("\"", "\\\"");
    }
}
