package org.example.poker.externalsystems.auditsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * External Audit System for storing poker hand comparison history.
 * 
 * This represents an EXTERNAL SYSTEM that our application integrates with.
 * In a real scenario, this could be:
 * - A third-party audit service
 * - A company-wide compliance system
 * - A separate microservice
 * 
 * For this kata, it's implemented as a simple in-memory system.
 * 
 * Key points:
 * - This is OUTSIDE our application's hexagon
 * - It has its own data model (AuditRecord)
 * - Our application adapts to it via an adapter
 * - We don't control this system's API or data format
 */
public class AuditSystem {
    
    private final Map<String, AuditRecord> storage = new HashMap<>();
    
    /**
     * Creates audit system.
     */
    public AuditSystem() {
        // Simple in-memory storage
    }
    
    /**
     * Stores an audit record.
     * This is the external system's API - we must adapt to it.
     */
    public void storeRecord(AuditRecord record) {
        if (record.getId() == null) {
            throw new IllegalArgumentException("AuditRecord must have an ID");
        }
        storage.put(record.getId(), record);
    }
    
    /**
     * Retrieves all audit records.
     * This is the external system's API - we must adapt to it.
     */
    public List<AuditRecord> retrieveAllRecords() {
        return new ArrayList<>(storage.values());
    }
    
    /**
     * Clears all audit records.
     * Useful for testing.
     */
    public void clearAllRecords() {
        storage.clear();
    }
}
