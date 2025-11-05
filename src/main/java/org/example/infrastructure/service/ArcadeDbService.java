package org.example.infrastructure.service;

import com.arcadedb.remote.RemoteServer;
import org.example.domain.model.DatabaseInfo;
import org.example.domain.service.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DatabaseService using ArcadeDB.
 * Follows Dependency Inversion Principle (DIP) - depends on abstractions.
 * Follows Single Responsibility Principle (SRP) - handles only database operations.
 */
@Service
public class ArcadeDbService implements DatabaseService {
    
    private static final Logger logger = LoggerFactory.getLogger(ArcadeDbService.class);
    
    private final RemoteServer remoteServer;
    
    public ArcadeDbService(RemoteServer remoteServer) {
        this.remoteServer = remoteServer;
    }
    
    @Override
    public List<DatabaseInfo> getAllDatabases() {
        logger.debug("Fetching all databases from ArcadeDB server");
        
        try {
            List<String> databaseNames = remoteServer.databases();
            
            List<DatabaseInfo> databases = databaseNames.stream()
                .map(DatabaseInfo::new)
                .collect(Collectors.toList());
            
            logger.info("Successfully retrieved {} databases", databases.size());
            return databases;
            
        } catch (Exception e) {
            logger.error("Error fetching databases: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to retrieve databases", e);
        }
    }
    
    @Override
    public boolean databaseExists(String databaseName) {
        logger.debug("Checking if database '{}' exists", databaseName);
        
        try {
            List<String> databases = remoteServer.databases();
            boolean exists = databases.contains(databaseName);
            
            logger.debug("Database '{}' exists: {}", databaseName, exists);
            return exists;
            
        } catch (Exception e) {
            logger.error("Error checking database existence: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to check database existence", e);
        }
    }
    
    @Override
    public DatabaseInfo createDatabase(String databaseName) {
        logger.info("Creating database '{}'", databaseName);
        
        try {
            if (databaseExists(databaseName)) {
                logger.warn("Database '{}' already exists", databaseName);
                throw new DatabaseAlreadyExistsException(
                    String.format("Database '%s' already exists", databaseName)
                );
            }
            
            remoteServer.create(databaseName);
            logger.info("Successfully created database '{}'", databaseName);
            
            return new DatabaseInfo(databaseName);
            
        } catch (DatabaseAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error creating database '{}': {}", databaseName, e.getMessage(), e);
            throw new DatabaseOperationException("Failed to create database", e);
        }
    }
}