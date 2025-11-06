package org.example.poker.app.service;

import org.example.poker.app.domain.ComparisonHistoryEntry;
import org.example.poker.app.port.in.GetComparisonHistoryUseCase;
import org.example.poker.app.port.out.ComparisonHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service implementing the get comparison history use case.
 * This service orchestrates the retrieval of historical comparisons.
 * 
 * Responsibilities:
 * - Delegate to repository to fetch all history
 * - Return domain objects directly
 * 
 * The service depends on:
 * - Outbound port (ComparisonHistoryRepository)
 * 
 * Note: Service depends on PORT interface, not implementation.
 * This is dependency inversion - domain defines what it needs.
 */
@Service
public class GetComparisonHistoryService implements GetComparisonHistoryUseCase {
    
    private final ComparisonHistoryRepository historyRepository;
    
    public GetComparisonHistoryService(ComparisonHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }
    
    @Override
    public List<ComparisonHistoryEntry> getHistory() {
        return historyRepository.findAll();
    }
}
