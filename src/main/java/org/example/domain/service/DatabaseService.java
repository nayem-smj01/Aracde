package org.example.domain.service;

import org.example.domain.model.DatabaseInfo;

import java.util.List;

/**
 * Service interface for database operations.
 * Follows Interface Segregation Principle (ISP).
 */
public interface DatabaseService {
    
    /**
     * Retrieves all available databases.
     * 
     * @return List of database information
     */
    List<DatabaseInfo> getAllDatabases();
    
    /**
     * Checks if a database exists.
     * 
     * @param databaseName the name of the database to check
     * @return true if database exists, false otherwise
     */
    boolean databaseExists(String databaseName);
    
    /**
     * Creates a new database.
     * 
     * @param databaseName the name of the database to create
     * @return information about the created database
     */
    DatabaseInfo createDatabase(String databaseName);
}