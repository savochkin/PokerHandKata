package org.example.poker.adapters.in.rest;

import org.example.poker.app.domain.ComparisonHistoryEntry;
import org.example.poker.app.port.in.GetComparisonHistoryUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST adapter for retrieving comparison history.
 * 
 * This is an inbound adapter that:
 * - Exposes HTTP endpoint for getting history
 * - Delegates to use case (via port)
 * - Maps domain objects to REST DTOs
 * 
 * Responsibilities:
 * - HTTP/REST concerns (endpoints, status codes, JSON)
 * - DTO mapping (domain → REST)
 * - Does NOT contain business logic
 */
@RestController
@RequestMapping("/api/poker/history")
public class HistoryController {
    
    private final GetComparisonHistoryUseCase getHistoryUseCase;
    
    public HistoryController(GetComparisonHistoryUseCase getHistoryUseCase) {
        this.getHistoryUseCase = getHistoryUseCase;
    }
    
    /**
     * GET /api/poker/history
     * 
     * Retrieves all comparison history.
     * 
     * @return List of history entries as JSON
     */
    @GetMapping
    public List<HistoryResponse> getHistory() {
        // Delegate to use case
        List<ComparisonHistoryEntry> history = getHistoryUseCase.getHistory();
        
        // Map domain objects to REST DTOs
        return history.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Maps domain object to REST DTO.
     * This keeps REST concerns out of the domain.
     */
    private HistoryResponse toResponse(ComparisonHistoryEntry entry) {
        return HistoryResponse.builder()
                .id(entry.getId())
                .blackHand(entry.getBlackHand())
                .whiteHand(entry.getWhiteHand())
                .winner(entry.getResult().getWinner().name())
                .description(entry.getResult().describe())
                .timestamp(entry.getTimestamp())
                .build();
    }
}
