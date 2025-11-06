package org.example.poker.app.port.in;

import org.example.poker.app.domain.ComparisonHistoryEntry;

import java.util.List;

/**
 * Inbound port for retrieving comparison history.
 * 
 * Use case: Get all past hand comparisons.
 * 
 * This port defines what the application provides to the outside world
 * for viewing historical comparisons.
 */
public interface GetComparisonHistoryUseCase {
    
    /**
     * Retrieves all comparison history entries.
     * 
     * @return List of all historical comparisons, ordered by timestamp (newest first)
     */
    List<ComparisonHistoryEntry> getHistory();
}
