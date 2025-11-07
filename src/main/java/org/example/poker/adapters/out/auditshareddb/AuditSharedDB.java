package org.example.poker.adapters.out.auditshareddb;

import org.example.poker.app.domain.ComparisonHistoryEntry;
import org.example.poker.app.port.out.ComparisonHistoryRepository;
import org.example.poker.externalsystems.auditdb.AuditSharedDBClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Shared Audit Database adapter implementing ComparisonHistoryRepository.
 * 
 * This is an OUTBOUND ADAPTER (driven adapter):
 * - Implements the port defined by our domain
 * - Integrates with shared Audit Database
 * - Maps between our domain objects and database format (key-value maps)
 * - Domain doesn't know about the database
 * 
 * Responsibilities:
 * - Implement the port interface (what our domain needs)
 * - Map domain ↔ database format (adapter's responsibility)
 * - Delegate to database client for storage operations
 * - Assign IDs to new entries (domain rule)
 * 
 * Note: This was the initial implementation. Later, the company decided
 * to move away from shared databases, leading to Task 2 (AuditSystemAdapter).
 */
@Repository
public class AuditSharedDB implements ComparisonHistoryRepository {
    
    private final AuditSharedDBClient dbClient;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    /**
     * Creates adapter with default database client.
     */
    public AuditSharedDB() {
        this.dbClient = new AuditSharedDBClient();
    }
    
    /**
     * Creates adapter with provided database client.
     * Useful for testing with a specific instance.
     */
    public AuditSharedDB(AuditSharedDBClient dbClient) {
        this.dbClient = dbClient;
    }
    
    @Override
    public ComparisonHistoryEntry save(ComparisonHistoryEntry entry) {
        // Generate ID if not present (domain rule: repository assigns IDs)
        ComparisonHistoryEntry entryWithId = entry.getId() == null
                ? entry.toBuilder().id(UUID.randomUUID().toString()).build()
                : entry;
        
        // Map domain to database format (adapter responsibility)
        Map<String, String> record = toDatabaseRecord(entryWithId);
        
        // Delegate to database client
        dbClient.storeRecord(entryWithId.getId(), record);
        
        return entryWithId;
    }
    
    @Override
    public List<ComparisonHistoryEntry> findAll() {
        // Retrieve from database
        List<Map<String, String>> records = dbClient.getAllRecords();
        
        // Map database format to domain (adapter responsibility)
        return records.stream()
                .map(this::toDomain)
                .sorted(Comparator.comparing(ComparisonHistoryEntry::getTimestamp).reversed())
                .toList();
    }
    
    /**
     * Maps domain object to database record format.
     * This is adapter responsibility - keeps domain pure.
     */
    private Map<String, String> toDatabaseRecord(ComparisonHistoryEntry entry) {
        Map<String, String> record = new HashMap<>();
        record.put("id", entry.getId());
        record.put("blackHand", entry.getBlackHand());
        record.put("whiteHand", entry.getWhiteHand());
        record.put("winner", entry.getResult().getWinner().name());
        record.put("winningRank", entry.getResult().getWinningRank() != null ? entry.getResult().getWinningRank().name() : null);
        record.put("losingRank", entry.getResult().getLosingRank() != null ? entry.getResult().getLosingRank().name() : null);
        record.put("category", entry.getResult().getCategory().name());
        record.put("timestamp", entry.getTimestamp().format(TIMESTAMP_FORMATTER));
        return record;
    }
    
    /**
     * Maps database record to domain object.
     * This is adapter responsibility - keeps domain pure.
     */
    private ComparisonHistoryEntry toDomain(Map<String, String> record) {
        return ComparisonHistoryEntry.builder()
                .id(record.get("id"))
                .blackHand(record.get("blackHand"))
                .whiteHand(record.get("whiteHand"))
                .result(org.example.poker.app.domain.ComparisonResult.builder()
                        .winner(org.example.poker.app.domain.Winner.valueOf(record.get("winner")))
                        .winningRank(record.get("winningRank") != null ? org.example.poker.app.domain.Rank.valueOf(record.get("winningRank")) : null)
                        .losingRank(record.get("losingRank") != null ? org.example.poker.app.domain.Rank.valueOf(record.get("losingRank")) : null)
                        .category(org.example.poker.app.domain.Category.valueOf(record.get("category")))
                        .build())
                .timestamp(LocalDateTime.parse(record.get("timestamp"), TIMESTAMP_FORMATTER))
                .build();
    }
}
