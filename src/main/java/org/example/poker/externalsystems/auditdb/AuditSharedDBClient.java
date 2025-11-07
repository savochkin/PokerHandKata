package org.example.poker.externalsystems.auditdb;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Shared Audit Database Client.
 * 
 * This represents an EXTERNAL SYSTEM - a shared database used by multiple teams
 * for audit and compliance purposes.
 * 
 * In a real scenario, this could be:
 * - A shared PostgreSQL/MySQL database
 * - A company-wide audit data warehouse
 * - A legacy system that multiple teams depend on
 * 
 * For this kata, it's implemented as a simple in-memory store.
 * 
 * Key points:
 * - This is OUTSIDE our application's hexagon
 * - It stores records in a generic format (Map<String, String>)
 * - Our application adapts to it via an adapter
 * - Multiple teams might use this same database
 * 
 * Note: This was the initial solution. Later, the company decided to move
 * away from shared databases, leading to the Audit System API (Task 2).
 */
public class AuditSharedDBClient {
    
    private final Map<String, Map<String, String>> storage = new ConcurrentHashMap<>();
    
    /**
     * Creates the shared database client.
     */
    public AuditSharedDBClient() {
        // Simple in-memory storage simulating a shared database
    }
    
    /**
     * Stores a record in the shared database.
     * Records are stored as key-value maps (simulating database rows).
     * 
     * @param id The record ID
     * @param record The record data as key-value pairs
     */
    public void storeRecord(String id, Map<String, String> record) {
        if (id == null) {
            throw new IllegalArgumentException("Record ID cannot be null");
        }
        storage.put(id, record);
    }
    
    /**
     * Retrieves a record by ID.
     * 
     * @param id The record ID
     * @return The record data, or null if not found
     */
    public Map<String, String> getRecord(String id) {
        return storage.get(id);
    }
    
    /**
     * Retrieves all records from the database.
     * 
     * @return List of all records
     */
    public List<Map<String, String>> getAllRecords() {
        return storage.values().stream()
                .collect(Collectors.toList());
    }
    
    /**
     * Clears all records.
     * Useful for testing.
     */
    public void clearAll() {
        storage.clear();
    }
}
