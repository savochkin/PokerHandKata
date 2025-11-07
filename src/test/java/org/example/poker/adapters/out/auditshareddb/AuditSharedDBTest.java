package org.example.poker.adapters.out.auditshareddb;

import org.example.poker.app.domain.Category;
import org.example.poker.app.domain.ComparisonHistoryEntry;
import org.example.poker.app.domain.ComparisonResult;
import org.example.poker.app.domain.Winner;
import org.example.poker.externalsystems.auditdb.AuditSharedDBClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for shared audit database adapter.
 * Tests the adapter implementation and integration with database client.
 */
@DisplayName("Audit Shared DB Adapter Tests")
class AuditSharedDBTest {
    
    private AuditSharedDB repository;
    private AuditSharedDBClient dbClient;
    
    @BeforeEach
    void setUp() {
        dbClient = new AuditSharedDBClient();
        repository = new AuditSharedDB(dbClient);
    }
    
    @AfterEach
    void tearDown() {
        dbClient.clearAll();
    }
    
    @Test
    @DisplayName("Should save entry and generate ID")
    void shouldSaveEntryAndGenerateId() {
        // Given: Entry without ID
        ComparisonResult result = ComparisonResult.builder()
                .winner(Winner.BLACK)
                .category(Category.HIGH_CARD)
                .build();
        
        ComparisonHistoryEntry entry = ComparisonHistoryEntry.builder()
                .blackHand("AH KD 9C 7D 4S")
                .whiteHand("KH QD 9C 7D 4S")
                .result(result)
                .timestamp(LocalDateTime.now())
                .build();
        
        // When: Save entry
        ComparisonHistoryEntry saved = repository.save(entry);
        
        // Then: ID is generated
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getBlackHand()).isEqualTo("AH KD 9C 7D 4S");
        assertThat(saved.getWhiteHand()).isEqualTo("KH QD 9C 7D 4S");
        assertThat(saved.getResult()).isEqualTo(result);
    }
    
    @Test
    @DisplayName("Should find all entries ordered by timestamp (newest first)")
    void shouldFindAllEntriesOrderedByTimestamp() {
        // Given: Multiple entries saved at different times
        LocalDateTime now = LocalDateTime.now();
        
        ComparisonResult result1 = ComparisonResult.builder().winner(Winner.BLACK).category(Category.HIGH_CARD).build();
        ComparisonResult result2 = ComparisonResult.builder().winner(Winner.WHITE).category(Category.ONE_PAIR).build();
        ComparisonResult result3 = ComparisonResult.builder().winner(Winner.TIE).category(Category.HIGH_CARD).build();
        
        ComparisonHistoryEntry entry1 = ComparisonHistoryEntry.builder()
                .blackHand("Hand1")
                .whiteHand("Hand1")
                .result(result1)
                .timestamp(now.minusMinutes(2))
                .build();
        
        ComparisonHistoryEntry entry2 = ComparisonHistoryEntry.builder()
                .blackHand("Hand2")
                .whiteHand("Hand2")
                .result(result2)
                .timestamp(now.minusMinutes(1))
                .build();
        
        ComparisonHistoryEntry entry3 = ComparisonHistoryEntry.builder()
                .blackHand("Hand3")
                .whiteHand("Hand3")
                .result(result3)
                .timestamp(now)
                .build();
        
        repository.save(entry1);
        repository.save(entry2);
        repository.save(entry3);
        
        // When: Find all
        List<ComparisonHistoryEntry> all = repository.findAll();
        
        // Then: Ordered by timestamp (newest first)
        assertThat(all).hasSize(3);
        assertThat(all.get(0).getBlackHand()).isEqualTo("Hand3");  // Newest
        assertThat(all.get(1).getBlackHand()).isEqualTo("Hand2");
        assertThat(all.get(2).getBlackHand()).isEqualTo("Hand1");  // Oldest
    }
    
    @Test
    @DisplayName("Should return empty list when no entries")
    void shouldReturnEmptyListWhenNoEntries() {
        // When: Find all with no entries
        List<ComparisonHistoryEntry> all = repository.findAll();
        
        // Then: Empty list
        assertThat(all).isEmpty();
    }
}
