
// ===================================
// ArcadeDbMonsterService.java
// ===================================
package org.example.infrastructure.service;

import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import org.example.domain.model.Monster;
import org.example.domain.service.MonsterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArcadeDbMonsterService implements MonsterService {
    
    private static final Logger logger = LoggerFactory.getLogger(ArcadeDbMonsterService.class);
    
    private final RemoteDatabase database;
    
    public ArcadeDbMonsterService(RemoteDatabase database) {
        this.database = database;
    }
    
    @Override
    public Monster createMonster(Monster monster) {
        logger.info("Creating monster: {}", monster.getName());
        
        String sql = String.format(
            "CREATE VERTEX Monster SET name = '%s', cr = %d, xp = %d, hp = %d, ac = %d, type = '%s'",
            escape(monster.getName()),
            monster.getCr(),
            monster.getXp(),
            monster.getHp(),
            monster.getAc(),
            escape(monster.getType())
        );
        
        try {
            ResultSet result = database.command("sql", sql);
            if (result.hasNext()) {
                Result record = result.next();
                monster.setId(record.getIdentity().get().toString());
                logger.info("Monster created with ID: {}", monster.getId());
            }
            return monster;
        } catch (Exception e) {
            logger.error("Error creating monster: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to create monster", e);
        }
    }
    
    @Override
    public Optional<Monster> getMonsterById(String id) {
        logger.debug("Fetching monster by ID: {}", id);
        
        String sql = String.format("SELECT FROM %s", id);
        
        try {
            ResultSet result = database.query("sql", sql);
            if (result.hasNext()) {
                return Optional.of(mapToMonster(result.next()));
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error fetching monster by ID: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to fetch monster", e);
        }
    }
    
    @Override
    public List<Monster> getAllMonsters() {
        logger.debug("Fetching all monsters");
        
        String sql = "SELECT FROM Monster";
        
        try {
            ResultSet result = database.query("sql", sql);
            List<Monster> monsters = new ArrayList<>();
            
            while (result.hasNext()) {
                monsters.add(mapToMonster(result.next()));
            }
            
            logger.info("Found {} monsters", monsters.size());
            return monsters;
        } catch (Exception e) {
            logger.error("Error fetching all monsters: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to fetch monsters", e);
        }
    }
    
    @Override
    public Monster updateMonster(String id, Monster monster) {
        logger.info("Updating monster: {}", id);
        
        String sql = String.format(
            "UPDATE %s SET name = '%s', cr = %d, xp = %d, hp = %d, ac = %d, type = '%s'",
            id,
            escape(monster.getName()),
            monster.getCr(),
            monster.getXp(),
            monster.getHp(),
            monster.getAc(),
            escape(monster.getType())
        );
        
        try {
            database.command("sql", sql);
            monster.setId(id);
            logger.info("Monster updated: {}", id);
            return monster;
        } catch (Exception e) {
            logger.error("Error updating monster: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to update monster", e);
        }
    }
    
    @Override
    public void deleteMonster(String id) {
        logger.info("Deleting monster: {}", id);
        
        String sql = String.format("DELETE VERTEX %s", id);
        
        try {
            database.command("sql", sql);
            logger.info("Monster deleted: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting monster: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to delete monster", e);
        }
    }
    
    @Override
    public List<Monster> getMonstersInRoom(String roomId) {
        logger.debug("Fetching monsters in room: {}", roomId);
        
        String sql = String.format("SELECT expand(in('LocatedIn')) FROM %s", roomId);
        
        try {
            ResultSet result = database.query("sql", sql);
            List<Monster> monsters = new ArrayList<>();
            
            while (result.hasNext()) {
                monsters.add(mapToMonster(result.next()));
            }
            
            logger.info("Found {} monsters in room", monsters.size());
            return monsters;
        } catch (Exception e) {
            logger.error("Error fetching monsters in room: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to fetch monsters in room", e);
        }
    }
    
    private Monster mapToMonster(Result record) {
        Monster monster = new Monster();
        monster.setId(record.getIdentity().get().toString());
        monster.setName(record.getProperty("name"));
        monster.setCr(record.getProperty("cr"));
        monster.setXp(record.getProperty("xp"));
        monster.setHp(record.getProperty("hp"));
        monster.setAc(record.getProperty("ac"));
        monster.setType(record.getProperty("type"));
        return monster;
    }
    
    private String escape(String value) {
        if (value == null) return "";
        return value.replace("'", "\\'").replace("\"", "\\\"");
    }
}
