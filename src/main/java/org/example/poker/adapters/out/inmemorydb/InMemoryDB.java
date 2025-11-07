package org.example.poker.adapters.out.inmemorydb;

import org.example.poker.app.domain.ComparisonHistoryEntry;
import org.example.poker.app.port.out.ComparisonHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of ComparisonHistoryRepository.
 * 
 * This is an outbound adapter (driven adapter) that implements
 * the port defined by the domain.
 * 
 * Uses ConcurrentHashMap for thread-safe in-memory storage.
 * Easy to swap for a real database implementation later.
 */
@Repository
public class InMemoryDB implements ComparisonHistoryRepository {
    
    private final Map<String, ComparisonHistoryEntry> storage = new ConcurrentHashMap<>();
    
    @Override
    public ComparisonHistoryEntry save(ComparisonHistoryEntry entry) {
        // Generate ID if not present
        String id = UUID.randomUUID().toString();
        
        // Create new entry with ID
        ComparisonHistoryEntry withId = entry.toBuilder()
                .id(id)
                .build();
        
        // Store
        storage.put(id, withId);
        
        return withId;
    }
    
    @Override
    public List<ComparisonHistoryEntry> findAll() {
        return storage.values().stream()
                .sorted(Comparator.comparing(ComparisonHistoryEntry::getTimestamp).reversed())
                .toList();
    }
}
