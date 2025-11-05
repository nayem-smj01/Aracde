package org.example.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "arcadedb")
@Validated
public class ArcadeDbProperties {
    
    @NotBlank(message = "ArcadeDB host must not be blank")
    private String host = "localhost";
    
    @Min(value = 1, message = "Port must be greater than 0")
    @Max(value = 65535, message = "Port must be less than 65536")
    private int port = 2480;
    
    @NotBlank(message = "Username must not be blank")
    private String username = "root";
    
    @NotBlank(message = "Password must not be blank")
    private String password = "arcadedb";
    
    private Connection connection = new Connection();
    
    public static class Connection {
        private int timeout = 5000;
        private Pool pool = new Pool();
        
        public static class Pool {
            private int maxSize = 10;
            
            public int getMaxSize() { return maxSize; }
            public void setMaxSize(int maxSize) { this.maxSize = maxSize; }
        }
        
        public int getTimeout() { return timeout; }
        public void setTimeout(int timeout) { this.timeout = timeout; }
        public Pool getPool() { return pool; }
        public void setPool(Pool pool) { this.pool = pool; }
    }
    
    // Getters and Setters
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Connection getConnection() { return connection; }
    public void setConnection(Connection connection) { this.connection = connection; }
}