// ===================================
// Alternative ArcadeDbConfig.java - More Robust
// ===================================
package org.example.config;

import com.arcadedb.remote.RemoteDatabase;
import com.arcadedb.remote.RemoteServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ArcadeDbProperties.class)
public class ArcadeDbConfig {

    private static final Logger logger = LoggerFactory.getLogger(ArcadeDbConfig.class);

    @Bean(destroyMethod = "close")
    public RemoteServer remoteServer(ArcadeDbProperties properties) {
        logger.info("Initializing ArcadeDB RemoteServer connection");
        logger.info("Connecting to {}:{}", properties.getHost(), properties.getPort());

        try {
            RemoteServer server = new RemoteServer(
                    properties.getHost(),
                    properties.getPort(),
                    properties.getUsername(),
                    properties.getPassword()
            );

            // Test connection
            logger.info("Testing connection...");
            server.databases();
            logger.info("Successfully connected to ArcadeDB server");

            // Try to create database, ignore if already exists
            ensureDatabaseExists(server, properties.getDatabase());

            return server;

        } catch (Exception e) {
            logger.error("Failed to connect to ArcadeDB at {}:{}",
                    properties.getHost(), properties.getPort());
            logger.error("Error: {}", e.getMessage());
            throw new IllegalStateException("Cannot connect to ArcadeDB", e);
        }
    }

    @Bean(destroyMethod = "close")
    public RemoteDatabase remoteDatabase(RemoteServer remoteServer, ArcadeDbProperties properties) {
        logger.info("Creating RemoteDatabase connection to: {}", properties.getDatabase());

        try {
            // Ensure database exists first
            ensureDatabaseExists(remoteServer, properties.getDatabase());

            // Small delay to ensure database is fully ready
            Thread.sleep(500);

            // Now create the database connection
            RemoteDatabase database = new RemoteDatabase(
                    properties.getHost(),
                    properties.getPort(),
                    properties.getDatabase(),
                    properties.getUsername(),
                    properties.getPassword()
            );

            // Verify connection works
            logger.info("Testing RemoteDatabase connection...");
            try {
                database.query("sql", "SELECT FROM schema:types LIMIT 1");
                logger.info("RemoteDatabase connection verified");
            } catch (Exception e) {
                logger.warn("Database query test failed (database might be new): {}", e.getMessage());
            }

            // Initialize schema
            initializeSchema(database);

            logger.info("RemoteDatabase bean created successfully");
            return database;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while creating RemoteDatabase", e);
        } catch (Exception e) {
            logger.error("Failed to create RemoteDatabase: {}", e.getMessage(), e);
            throw new IllegalStateException("Cannot create RemoteDatabase connection", e);
        }
    }

    private void ensureDatabaseExists(RemoteServer server, String databaseName) {
        try {
            var databases = server.databases();
            logger.info("Existing databases: {}", databases);

            if (!databases.contains(databaseName)) {
                logger.info("Database '{}' not found. Creating...", databaseName);
                try {
                    server.create(databaseName);
                    logger.info("Database '{}' created successfully", databaseName);

                    // Wait a bit for database to be fully initialized
                    Thread.sleep(1000);

                    // Verify it was created
                    databases = server.databases();
                    if (databases.contains(databaseName)) {
                        logger.info("Verified database '{}' exists in server", databaseName);
                    } else {
                        logger.error("Database '{}' not found after creation!", databaseName);
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ie);
                }
            } else {
                logger.info("Database '{}' already exists", databaseName);
            }
        } catch (Exception e) {
            // If error contains "already exists", it's not a problem
            String errorMsg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (errorMsg.contains("already exists")) {
                logger.info("Database '{}' already exists (confirmed via error)", databaseName);
            } else {
                logger.error("Error ensuring database exists: {}", e.getMessage());
                throw new RuntimeException("Failed to ensure database exists", e);
            }
        }
    }

    private void initializeSchema(RemoteDatabase database) {
        logger.info("Initializing database schema...");

        try {
            // Begin a transaction for schema creation
            database.transaction(() -> {
                // Create Vertex Types
                executeSchemaCommand(database, "CREATE VERTEX TYPE Room IF NOT EXISTS");
                executeSchemaCommand(database, "CREATE PROPERTY Room.name IF NOT EXISTS STRING");
                executeSchemaCommand(database, "CREATE PROPERTY Room.description IF NOT EXISTS STRING");
                executeSchemaCommand(database, "CREATE PROPERTY Room.size IF NOT EXISTS STRING");
                executeSchemaCommand(database, "CREATE PROPERTY Room.lightLevel IF NOT EXISTS STRING");

                executeSchemaCommand(database, "CREATE VERTEX TYPE Monster IF NOT EXISTS");
                executeSchemaCommand(database, "CREATE PROPERTY Monster.name IF NOT EXISTS STRING");
                executeSchemaCommand(database, "CREATE PROPERTY Monster.cr IF NOT EXISTS INTEGER");
                executeSchemaCommand(database, "CREATE PROPERTY Monster.xp IF NOT EXISTS INTEGER");
                executeSchemaCommand(database, "CREATE PROPERTY Monster.hp IF NOT EXISTS INTEGER");
                executeSchemaCommand(database, "CREATE PROPERTY Monster.ac IF NOT EXISTS INTEGER");
                executeSchemaCommand(database, "CREATE PROPERTY Monster.type IF NOT EXISTS STRING");

                executeSchemaCommand(database, "CREATE VERTEX TYPE Treasure IF NOT EXISTS");
                executeSchemaCommand(database, "CREATE PROPERTY Treasure.name IF NOT EXISTS STRING");
                executeSchemaCommand(database, "CREATE PROPERTY Treasure.gp IF NOT EXISTS INTEGER");
                executeSchemaCommand(database, "CREATE PROPERTY Treasure.type IF NOT EXISTS STRING");
                executeSchemaCommand(database, "CREATE PROPERTY Treasure.rarity IF NOT EXISTS STRING");
                executeSchemaCommand(database, "CREATE PROPERTY Treasure.description IF NOT EXISTS STRING");

                // Create Edge Types
                executeSchemaCommand(database, "CREATE EDGE TYPE ConnectsTo IF NOT EXISTS");
                executeSchemaCommand(database, "CREATE PROPERTY ConnectsTo.direction IF NOT EXISTS STRING");
                executeSchemaCommand(database, "CREATE PROPERTY ConnectsTo.doorType IF NOT EXISTS STRING");
                executeSchemaCommand(database, "CREATE PROPERTY ConnectsTo.distance IF NOT EXISTS INTEGER");

                executeSchemaCommand(database, "CREATE EDGE TYPE LocatedIn IF NOT EXISTS");
                executeSchemaCommand(database, "CREATE PROPERTY LocatedIn.quantity IF NOT EXISTS INTEGER");
                executeSchemaCommand(database, "CREATE PROPERTY LocatedIn.behavior IF NOT EXISTS STRING");

                executeSchemaCommand(database, "CREATE EDGE TYPE StoredIn IF NOT EXISTS");
                executeSchemaCommand(database, "CREATE PROPERTY StoredIn.hidden IF NOT EXISTS BOOLEAN");
                executeSchemaCommand(database, "CREATE PROPERTY StoredIn.containerType IF NOT EXISTS STRING");

                executeSchemaCommand(database, "CREATE EDGE TYPE Guards IF NOT EXISTS");
                executeSchemaCommand(database, "CREATE PROPERTY Guards.awareness IF NOT EXISTS STRING");
                executeSchemaCommand(database, "CREATE PROPERTY Guards.priority IF NOT EXISTS INTEGER");
            });

            logger.info("Schema initialized successfully");

        } catch (Exception e) {
            logger.warn("Schema initialization completed with warnings: {}", e.getMessage());
            // Don't throw - schema might already exist
        }
    }

    private void executeSchemaCommand(RemoteDatabase database, String sql) {
        try {
            database.command("sql", sql);
            logger.debug("Executed: {}", sql);
        } catch (Exception e) {
            // Log but don't fail - property might already exist
            if (!e.getMessage().contains("already exists")) {
                logger.debug("Schema command '{}' resulted in: {}", sql, e.getMessage());
            }
        }
    }
}