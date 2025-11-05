package org.example;

import com.arcadedb.database.Database;
import com.arcadedb.database.DatabaseFactory;
import com.arcadedb.query.sql.executor.ResultSet;

import java.io.File;

/**
 * Creates an ArcadeDB database and imports USDA Food Data using SQL IMPORT DATABASE command.
 */
public class FoodDataImporter {
    
    private static final String DATABASE_NAME = "jevan";
    private static final String DATABASE_PATH = "./databases/" + DATABASE_NAME;
    private static final String FOOD_DATA_URL = "https://fdc.nal.usda.gov/fdc-datasets/FoodData_Central_foundation_food_json_2022-10-28.zip";
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("ArcadeDB Food Data Importer");
        System.out.println("=".repeat(80));
        
        try {
            // Step 1: Create or open the database
            Database database = createDatabase();
            
            // Step 2: Import the food data using SQL command
            importFoodData(database);
            
            // Step 3: Display statistics
            displayStatistics(database);
            
            // Close the database
            database.close();
            
            System.out.println("\n" + "=".repeat(80));
            System.out.println("Import completed successfully!");
            System.out.println("Database location: " + DATABASE_PATH);
            System.out.println("=".repeat(80));
            
        } catch (Exception e) {
            System.err.println("\nERROR: Import failed!");
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Creates a new database or opens existing one.
     */
    private static Database createDatabase() {
        System.out.println("\n[1/3] Creating/Opening Database");
        System.out.println("-".repeat(80));
        
        File dbFile = new File(DATABASE_PATH);
        DatabaseFactory factory = new DatabaseFactory(DATABASE_PATH);
        
        Database database;
        if (dbFile.exists()) {
            System.out.println("Database already exists, deleting...");
            deleteDirectory(dbFile);
            System.out.println("Creating new database: " + DATABASE_NAME);
            database = factory.create();
        } else {
            System.out.println("Creating new database: " + DATABASE_NAME);
            database = factory.create();
        }
        
        System.out.println("✓ Database ready at: " + DATABASE_PATH);
        return database;
    }
    
    /**
     * Imports food data using SQL IMPORT DATABASE command with mapping.
     */
    private static void importFoodData(Database database) {
        System.out.println("\n[2/3] Importing Food Data");
        System.out.println("-".repeat(80));
        System.out.println("Source: " + FOOD_DATA_URL);
        System.out.println("This may take several minutes...\n");
        
        // Build the SQL IMPORT DATABASE command with the mapping
        String importCommand = buildImportCommand();
        
        try {
            // Execute the import command
            database.begin();
            ResultSet result = database.command("sql", importCommand);
            database.commit();
            
            System.out.println("\n✓ Import command executed successfully");
            
        } catch (Exception e) {
            database.rollback();
            System.err.println("✗ Import failed: " + e.getMessage());
            throw new RuntimeException("Failed to import food data", e);
        }
    }
    
    /**
     * Builds the SQL IMPORT DATABASE command with the complete mapping.
     */
    private static String buildImportCommand() {
        // The mapping JSON needs to be embedded in the SQL command
        String mapping = """
        {
            "FoundationFoods": [{
                "@cat": "v",
                "@type": "<foodClass>",
                "foodNutrients": [{
                    "@cat": "e",
                    "@type": "HAS_NUTRIENT",
                    "@in": "nutrient",
                    "@cardinality": "no-duplicates",
                    "nutrient": {
                        "@cat": "v",
                        "@type": "Nutrient",
                        "@id": "id",
                        "@idType": "long",
                        "@strategy": "merge"
                    },
                    "foodNutrientDerivation": "@ignore"
                }],
                "inputFoods": [{
                    "@cat": "e",
                    "@type": "INPUT",
                    "@in": "inputFood",
                    "@cardinality": "no-duplicates",
                    "inputFood": {
                        "@cat": "v",
                        "@type": "<foodClass>",
                        "@id": "fdcId",
                        "@idType": "long",
                        "@strategy": "merge",
                        "foodCategory": {
                            "@cat": "e",
                            "@type": "HAS_CATEGORY",
                            "@cardinality": "no-duplicates",
                            "@in": {
                                "@cat": "v",
                                "@type": "FoodCategory",
                                "@id": "id",
                                "@idType": "long",
                                "@strategy": "merge"
                            }
                        }
                    }
                }]
            }]
        }
        """.replace("\n", "").replace("  ", "");
        
        return String.format("IMPORT DATABASE %s WITH mapping = %s", FOOD_DATA_URL, mapping);
    }
    
    /**
     * Displays database statistics after import.
     */
    private static void displayStatistics(Database database) {
        System.out.println("\n[3/3] Database Statistics");
        System.out.println("-".repeat(80));
        
        try {
            // Query vertex types and counts
            ResultSet types = database.query("sql", 
                "SELECT name, count FROM (SELECT name, count(*) as count FROM schema:types WHERE type = 'vertex' GROUP BY name)");
            
            System.out.println("Vertex Types:");
            while (types.hasNext()) {
                var record = types.next();
                System.out.printf("  - %-20s : %,d records%n", 
                    record.getProperty("name"), 
                    record.getProperty("count"));
            }
            
            // Query edge types and counts
            ResultSet edgeTypes = database.query("sql", 
                "SELECT name FROM schema:types WHERE type = 'edge'");
            
            System.out.println("\nEdge Types:");
            while (edgeTypes.hasNext()) {
                var record = edgeTypes.next();
                String edgeName = record.getProperty("name");
                ResultSet edgeCount = database.query("sql", 
                    String.format("SELECT count(*) as count FROM `%s`", edgeName));
                if (edgeCount.hasNext()) {
                    System.out.printf("  - %-20s : %,d edges%n", 
                        edgeName, 
                        edgeCount.next().getProperty("count"));
                }
            }
            
        } catch (Exception e) {
            System.out.println("Could not retrieve statistics: " + e.getMessage());
        }
    }
    
    /**
     * Recursively deletes a directory and its contents.
     */
    private static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}
