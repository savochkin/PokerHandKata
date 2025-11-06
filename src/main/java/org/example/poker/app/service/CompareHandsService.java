package org.example.poker.app.service;

import org.example.poker.app.port.in.CompareHandsUseCase;
import org.example.poker.app.port.out.ComparisonHistoryRepository;
import org.example.poker.app.domain.ComparisonHistoryEntry;
import org.example.poker.app.domain.ComparisonResult;
import org.example.poker.app.domain.Hand;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Application service implementing the compare hands use case.
 * This service orchestrates the domain logic and is part of the application layer.
 * 
 * Responsibilities:
 * - Parse hands using domain model
 * - Execute comparison logic
 * - Save comparison to history (via outbound port)
 * - Return domain objects directly
 * 
 * The service depends on:
 * - Domain model (Hand)
 * - Outbound port (ComparisonHistoryRepository)
 * 
 * Note: Service depends on PORT interface, not implementation.
 * This is dependency inversion - domain defines what it needs.
 */
@Service
public class CompareHandsService implements CompareHandsUseCase {
    
    private final ComparisonHistoryRepository historyRepository;
    
    public CompareHandsService(ComparisonHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }
    
    @Override
    public ComparisonResult compareHands(String blackHand, String whiteHand) {
        // Parse hands using domain model
        Hand black = Hand.parse(blackHand);
        Hand white = Hand.parse(whiteHand);
        
        // Execute domain logic
        ComparisonResult result = black.compare(white);
        
        // Save to history (orchestration responsibility)
        ComparisonHistoryEntry entry = ComparisonHistoryEntry.builder()
                .blackHand(blackHand)
                .whiteHand(whiteHand)
                .result(result)
                .timestamp(LocalDateTime.now())
                .build();
        
        historyRepository.save(entry);
        
        // Return domain result
        return result;
    }
}
