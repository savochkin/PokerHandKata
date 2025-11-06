package org.example.poker.app.service;

import org.example.poker.app.domain.ComparisonHistoryEntry;
import org.example.poker.app.domain.ComparisonResult;
import org.example.poker.app.domain.Winner;
import org.example.poker.app.port.in.GetComparisonHistoryUseCase;
import org.example.poker.app.port.out.ComparisonHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit test for GetComparisonHistoryService.
 * Tests the service orchestration (no Spring context needed).
 * 
 * Focus: Verify that the service correctly orchestrates:
 * - Delegates to repository to fetch history
 * - Returns domain objects
 */
@DisplayName("GetComparisonHistoryService - Orchestration Tests")
class GetComparisonHistoryServiceTest {
    
    private ComparisonHistoryRepository mockRepository;
    private GetComparisonHistoryUseCase useCase;
    
    @BeforeEach
    void setUp() {
        mockRepository = mock(ComparisonHistoryRepository.class);
        useCase = new GetComparisonHistoryService(mockRepository);
    }
    
    @Test
    @DisplayName("Should delegate to repository and return all history entries")
    void shouldDelegateToRepositoryAndReturnAllHistoryEntries() {
        // Given: Repository has some history entries
        ComparisonResult result1 = ComparisonResult.builder()
                .winner(Winner.BLACK)
                .category(org.example.poker.app.domain.Category.HIGH_CARD)
                .build();
        ComparisonResult result2 = ComparisonResult.builder()
                .winner(Winner.WHITE)
                .category(org.example.poker.app.domain.Category.ONE_PAIR)
                .build();
        
        ComparisonHistoryEntry entry1 = ComparisonHistoryEntry.builder()
                .id("id1")
                .blackHand("AH KD 9C 7D 4S")
                .whiteHand("KH QD 9C 7D 4S")
                .result(result1)
                .timestamp(LocalDateTime.now())
                .build();
        
        ComparisonHistoryEntry entry2 = ComparisonHistoryEntry.builder()
                .id("id2")
                .blackHand("2H 3D 5S 9C KD")
                .whiteHand("2D 3H 5C 9S AH")
                .result(result2)
                .timestamp(LocalDateTime.now())
                .build();
        
        when(mockRepository.findAll()).thenReturn(List.of(entry1, entry2));
        
        // When: Use case retrieves history
        List<ComparisonHistoryEntry> history = useCase.getHistory();
        
        // Then: Returns all entries from repository
        assertThat(history).hasSize(2);
        assertThat(history).containsExactly(entry1, entry2);
        verify(mockRepository).findAll();
    }
    
    @Test
    @DisplayName("Should return empty list when no history exists")
    void shouldReturnEmptyListWhenNoHistoryExists() {
        // Given: Repository has no entries
        when(mockRepository.findAll()).thenReturn(List.of());
        
        // When: Use case retrieves history
        List<ComparisonHistoryEntry> history = useCase.getHistory();
        
        // Then: Returns empty list
        assertThat(history).isEmpty();
        verify(mockRepository).findAll();
    }
}
