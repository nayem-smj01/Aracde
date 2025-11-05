package org.example.config;

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
    
    @Bean
    public RemoteServer remoteServer(ArcadeDbProperties properties) {
        logger.info("Initializing ArcadeDB RemoteServer connection to {}:{}", 
                    properties.getHost(), properties.getPort());
        
        try {
            RemoteServer server = new RemoteServer(
                properties.getHost(),
                properties.getPort(),
                properties.getUsername(),
                properties.getPassword()
            );
            
            logger.info("Successfully connected to ArcadeDB server");
            return server;
            
        } catch (Exception e) {
            logger.error("Failed to connect to ArcadeDB server: {}", e.getMessage(), e);
            throw new IllegalStateException("Unable to establish ArcadeDB connection", e);
        }
    }
}
