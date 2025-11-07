package org.example.poker.adapters.out.auditsystem;

//import org.example.poker.app.domain.Category;
//import org.example.poker.app.domain.ComparisonHistoryEntry;
//import org.example.poker.app.domain.ComparisonResult;
//import org.example.poker.app.domain.Winner;
//import org.example.poker.externalsystems.auditsystem.AuditSystem;
import org.junit.jupiter.api.*;

//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;

/**
 * REFERENCE SOLUTION for Task 2
 * 
 * Tests for AuditSystemAdapter.
 * 
 * These are ADAPTER tests - they test the integration with the external system,
 * not business logic.
 * 
 * What we test:
 * - Adapter correctly implements the port interface
 * - Mapping between domain and external system models works
 * - Integration with external Audit System works correctly
 * - Domain objects are persisted and retrieved correctly
 */
@DisplayName("AuditSystemAdapter - Outbound Adapter Tests")
@Disabled
class AuditSystemAdapterTest {
  /*
    private AuditSystemAdapter repository;
    private AuditSystem auditSystem;
    
    @BeforeEach
    void setUp() {
        auditSystem = new AuditSystem();
        repository = new AuditSystemAdapter(auditSystem);
    }
    
    @AfterEach
    void tearDown() {
        // Cleanup audit system
        auditSystem.clearAllRecords();
    }
    
    @Test
    @DisplayName("Should save entry to file and assign ID")
    void shouldSaveEntryToFileAndAssignId() {
        // Given: A history entry without ID
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
        
        // When: Entry is saved
        ComparisonHistoryEntry saved = repository.save(entry);
        
        // Then: ID is assigned
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getBlackHand()).isEqualTo("AH KD 9C 7D 4S");
        assertThat(saved.getWhiteHand()).isEqualTo("KH QD 9C 7D 4S");
    }
    
    @Test
    @DisplayName("Should persist entry to external system")
    void shouldPersistEntryToExternalSystem() {
        // Given: A history entry
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
        
        // When: Entry is saved
        ComparisonHistoryEntry saved = repository.save(entry);
        
        // Then: Entry can be retrieved from external system
        List<ComparisonHistoryEntry> retrieved = repository.findAll();
        assertThat(retrieved).hasSize(1);
        assertThat(retrieved.get(0).getId()).isEqualTo(saved.getId());
        assertThat(retrieved.get(0).getBlackHand()).isEqualTo("AH KD 9C 7D 4S");
        assertThat(retrieved.get(0).getWhiteHand()).isEqualTo("KH QD 9C 7D 4S");
    }
    
    @Test
    @DisplayName("Should retrieve all saved entries")
    void shouldRetrieveAllSavedEntries() {
        // Given: Multiple entries saved
        ComparisonResult result1 = ComparisonResult.builder()
                .winner(Winner.BLACK)
                .category(Category.HIGH_CARD)
                .build();
        ComparisonResult result2 = ComparisonResult.builder()
                .winner(Winner.WHITE)
                .category(Category.ONE_PAIR)
                .build();
        
        ComparisonHistoryEntry entry1 = ComparisonHistoryEntry.builder()
                .blackHand("AH KD 9C 7D 4S")
                .whiteHand("KH QD 9C 7D 4S")
                .result(result1)
                .timestamp(LocalDateTime.now())
                .build();
        
        ComparisonHistoryEntry entry2 = ComparisonHistoryEntry.builder()
                .blackHand("2H 2D 5S 9C KD")
                .whiteHand("3C 4H 5C 8C AH")
                .result(result2)
                .timestamp(LocalDateTime.now())
                .build();
        
        repository.save(entry1);
        repository.save(entry2);
        
        // When: All entries are retrieved
        List<ComparisonHistoryEntry> entries = repository.findAll();
        
        // Then: Both entries are returned (order may vary)
        assertThat(entries).hasSize(2);
        assertThat(entries)
                .extracting(ComparisonHistoryEntry::getBlackHand)
                .containsExactlyInAnyOrder("AH KD 9C 7D 4S", "2H 2D 5S 9C KD");
    }
    
    @Test
    @DisplayName("Should return empty list when no entries exist")
    void shouldReturnEmptyListWhenNoEntriesExist() {
        // When: Retrieving from empty file
        List<ComparisonHistoryEntry> entries = repository.findAll();
        
        // Then: Empty list is returned
        assertThat(entries).isEmpty();
    }
    
    @Test
    @DisplayName("Should preserve all entry fields")
    void shouldPreserveAllEntryFields() {
        // Given: Entry with all fields populated
        LocalDateTime timestamp = LocalDateTime.of(2025, 11, 7, 10, 30);
        ComparisonResult result = ComparisonResult.builder()
                .winner(Winner.BLACK)
                .category(Category.FULL_HOUSE)
                .build();
        
        ComparisonHistoryEntry entry = ComparisonHistoryEntry.builder()
                .blackHand("2H 4S 4C 2D 4H")
                .whiteHand("2S 8S AS QS 3S")
                .result(result)
                .timestamp(timestamp)
                .build();
        
        // When: Entry is saved and retrieved
        repository.save(entry);
        List<ComparisonHistoryEntry> retrieved = repository.findAll();
        
        // Then: All fields are preserved
        assertThat(retrieved).hasSize(1);
        ComparisonHistoryEntry retrievedEntry = retrieved.get(0);
        assertThat(retrievedEntry.getBlackHand()).isEqualTo("2H 4S 4C 2D 4H");
        assertThat(retrievedEntry.getWhiteHand()).isEqualTo("2S 8S AS QS 3S");
        assertThat(retrievedEntry.getResult().getWinner()).isEqualTo(Winner.BLACK);
        assertThat(retrievedEntry.getResult().getCategory()).isEqualTo(Category.FULL_HOUSE);
        assertThat(retrievedEntry.getTimestamp()).isEqualTo(timestamp);
    }
    
    @Test
    @DisplayName("Should handle multiple save operations")
    void shouldHandleMultipleSaveOperations() {
        // Given: Multiple save operations
        ComparisonResult result = ComparisonResult.builder()
                .winner(Winner.BLACK)
                .category(Category.HIGH_CARD)
                .build();
        
        // When: Saving multiple times
        for (int i = 0; i < 5; i++) {
            ComparisonHistoryEntry entry = ComparisonHistoryEntry.builder()
                    .blackHand("AH KD 9C 7D 4S")
                    .whiteHand("KH QD 9C 7D 4S")
                    .result(result)
                    .timestamp(LocalDateTime.now())
                    .build();
            repository.save(entry);
        }
        
        // Then: All entries are persisted
        List<ComparisonHistoryEntry> entries = repository.findAll();
        assertThat(entries).hasSize(5);
    }*/
}
