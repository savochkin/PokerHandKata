package org.example.poker.app.port.out;

import org.example.poker.app.domain.ComparisonHistoryEntry;

import java.util.List;

/**
 * Outbound port for persisting comparison history.
 * 
 * This port defines what the domain NEEDS from the outside world.
 * The domain doesn't care HOW history is stored (in-memory, database, file),
 * it just defines the contract.
 * 
 * Adapters (driven/outbound) will implement this interface.
 */
public interface ComparisonHistoryRepository {
    
    /**
     * Save a comparison history entry.
     * 
     * @param entry The entry to save (id may be null)
     * @return The saved entry with generated ID
     */
    ComparisonHistoryEntry save(ComparisonHistoryEntry entry);
    
    /**
     * Find all comparison history entries.
     * 
     * @return List of all entries, ordered by timestamp (newest first)
     */
    List<ComparisonHistoryEntry> findAll();
}
