package org.example.poker.app.service;

import org.example.poker.app.domain.ComparisonHistoryEntry;
import org.example.poker.app.domain.HandComparisonService;
import org.example.poker.app.port.in.CompareHandsUseCase;
import org.example.poker.app.port.in.GetComparisonHistoryUseCase;
import org.example.poker.app.port.out.ComparisonHistoryRepository;
import org.example.poker.app.domain.ComparisonResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service implementing poker hand comparison use cases.
 * This is a THIN application layer that delegates to domain services.
 * 
 * Responsibilities:
 * - Expose use cases to adapters (inbound port implementations)
 * - Delegate to domain service
 * - Wire Spring dependencies
 * 
 * Note: The actual business logic lives in the domain service
 * (HandComparisonService), not here.
 */
@Service
public class CompareHandsService implements CompareHandsUseCase, GetComparisonHistoryUseCase {
    
    private final HandComparisonService domainService;
    
    public CompareHandsService(ComparisonHistoryRepository historyRepository) {
        // Create domain service (no Spring in domain layer)
        this.domainService = new HandComparisonService(historyRepository);
    }
    
    @Override
    public ComparisonResult compareHands(String blackHand, String whiteHand) {
        // Delegate to domain service
        return domainService.compareAndRecord(blackHand, whiteHand);
    }
    
    @Override
    public List<ComparisonHistoryEntry> getHistory() {
        // Delegate to domain service
        return domainService.getAllHistory();
    }
}
