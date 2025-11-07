package org.example.poker.app.domain;

import org.example.poker.app.port.out.ComparisonHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain service for poker hand comparison operations.
 * 
 * This is a DOMAIN SERVICE (not an application service):
 * - Contains domain logic that doesn't naturally fit in an entity
 * - Enforces domain rules (e.g., "all comparisons must be recorded")
 * - Has NO Spring annotations (pure domain)
 * - Uses domain port (ComparisonHistoryRepository) defined by the domain
 * 
 * Domain Rules:
 * - Every hand comparison must be recorded for audit/analytics
 * - History is ordered by timestamp (newest first)
 */
public class HandComparisonService {
    
    private final ComparisonHistoryRepository repository;
    
    public HandComparisonService(ComparisonHistoryRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Compares two poker hands and records the comparison.
     * 
     * Domain logic:
     * 1. Parse hand strings to domain objects
     * 2. Execute comparison (poker rules)
     * 3. Record the comparison (domain rule: all comparisons must be recorded)
     * 
     * @param blackHand Black player's hand as string
     * @param whiteHand White player's hand as string
     * @return Comparison result
     * @throws IllegalArgumentException if hands are invalid
     */
    public ComparisonResult compareAndRecord(String blackHand, String whiteHand) {
        // Parse hands (validates format)
        Hand black = Hand.parse(blackHand);
        Hand white = Hand.parse(whiteHand);
        
        // Execute comparison (pure domain logic)
        ComparisonResult result = black.compare(white);
        
        // Record comparison (domain rule: all comparisons must be recorded)
        ComparisonHistoryEntry entry = ComparisonHistoryEntry.builder()
                .blackHand(blackHand)
                .whiteHand(whiteHand)
                .result(result)
                .timestamp(LocalDateTime.now())
                .build();
        
        repository.save(entry);
        
        return result;
    }
    
    /**
     * Retrieves all comparison history entries.
     * 
     * @return List of all historical comparisons, ordered by timestamp (newest first)
     */
    public List<ComparisonHistoryEntry> getAllHistory() {
        return repository.findAll();
    }
}
