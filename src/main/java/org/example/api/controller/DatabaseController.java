package org.example.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.api.dto.CreateDatabaseRequest;
import org.example.api.dto.DatabaseDto;
import org.example.api.dto.DatabaseResponse;
import org.example.domain.model.DatabaseInfo;
import org.example.domain.service.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for database operations.
 * Follows Single Responsibility Principle (SRP) - handles only HTTP requests/responses.
 * Follows Open/Closed Principle (OCP) - extensible through dependency injection.
 */
@RestController
@RequestMapping("/api/v1/databases")
@Tag(name = "Database Management", description = "APIs for managing ArcadeDB databases")
public class DatabaseController {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseController.class);

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * GET /api/v1/databases
     * Retrieves all available databases.
     */
    @GetMapping
    @Operation(
            summary = "Get all databases",
            description = "Retrieves a list of all available databases from the ArcadeDB server"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved databases",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<DatabaseResponse> getAllDatabases() {
        logger.info("Received request to fetch all databases");

        List<DatabaseInfo> databases = databaseService.getAllDatabases();

        List<DatabaseDto> databaseDtos = databases.stream()
                .map(db -> new DatabaseDto(db.getName()))
                .collect(Collectors.toList());

        DatabaseResponse response = new DatabaseResponse(databaseDtos);

        logger.info("Returning {} databases", response.getCount());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/databases/{name}/exists
     * Checks if a database exists.
     */
    @GetMapping("/{name}/exists")
    @Operation(
            summary = "Check if database exists",
            description = "Checks whether a database with the given name exists on the server"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully checked database existence",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
                    )
            )
    })
    public ResponseEntity<Boolean> checkDatabaseExists(
            @Parameter(description = "Name of the database to check", required = true)
            @PathVariable String name) {

        logger.info("Checking if database '{}' exists", name);

        boolean exists = databaseService.databaseExists(name);

        return ResponseEntity.ok(exists);
    }

    /**
     * POST /api/v1/databases
     * Creates a new database.
     */
    @PostMapping
    @Operation(
            summary = "Create a new database",
            description = "Creates a new database with the specified name"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Database created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Database already exists"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<DatabaseDto> createDatabase(
            @Parameter(description = "Database creation request", required = true)
            @Valid @RequestBody CreateDatabaseRequest request) {

        logger.info("Received request to create database '{}'", request.getName());

        DatabaseInfo created = databaseService.createDatabase(request.getName());
        DatabaseDto response = new DatabaseDto(created.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}