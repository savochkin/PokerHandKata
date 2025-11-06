package org.example.poker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application for the Poker Hand Comparison service.
 * 
 * This application demonstrates hexagonal architecture:
 * - Domain layer: Hand, Card, Rank, etc. (pure business logic)
 * - Application layer: Services implementing use cases
 * - Adapters layer: REST controllers, CLI, repositories
 * 
 * To run:
 * mvn spring-boot:run
 * 
 * Or:
 * java -jar target/PokerHandKata-1.0-SNAPSHOT.jar
 */
@SpringBootApplication
public class PokerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PokerApplication.class, args);
    }
}
