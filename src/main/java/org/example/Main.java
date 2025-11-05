package org.example;


import com.arcadedb.remote.RemoteDatabase;
import com.arcadedb.remote.RemoteServer;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Connection details
        String host = "localhost";
        int port = 2480;
        String username = "root";
        String password = "2cb866ef";

        try {
            // Connect to ArcadeDB Server
            System.out.println("Connecting to ArcadeDB server at " + host + ":" + port);
            RemoteServer server = new RemoteServer(host, port, username, password);

            // List all databases
            System.out.println("\n=== Available Databases ===");
            List<String> databases = server.databases();

            if (databases.isEmpty()) {
                System.out.println("No databases found on the server.");
            } else {
                for (String dbName : databases) {
                    System.out.println("- " + dbName);
                }
            }

            // Example: Connect to a specific database (if it exists)
            if (!databases.isEmpty()) {
                String firstDb = databases.get(0);
                System.out.println("\n=== Connecting to database: " + firstDb + " ===");

                RemoteDatabase db = new RemoteDatabase(host, port, firstDb, username, password);

                System.out.println("Successfully connected to database: " + firstDb);
                //System.out.println("Database exists: " + db.);

                // Close the database connection
                db.close();
                System.out.println("Database connection closed.");
            }

            // Example: Create a new database
            String newDbName = "testdb";
            if (!databases.contains(newDbName)) {
                System.out.println("\n=== Creating new database: " + newDbName + " ===");
                server.create(newDbName);
                System.out.println("Database created successfully!");

                // Connect to the newly created database
                RemoteDatabase newDb = new RemoteDatabase(host, port, newDbName, username, password);
                System.out.println("Connected to new database: " + newDbName);
                newDb.close();
            }

            // Close server connection
            server.close();
            System.out.println("\nServer connection closed.");

        } catch (Exception e) {
            System.err.println("Error connecting to ArcadeDB: " + e.getMessage());
            e.printStackTrace();
        }
    }
}