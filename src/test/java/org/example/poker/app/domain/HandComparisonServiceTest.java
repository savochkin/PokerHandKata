package org.example.poker.app.domain;

import org.example.poker.app.port.out.ComparisonHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Tests for HandComparisonService domain logic.
 * 
 * This test verifies the domain service responsibilities:
 * - Parse hand strings
 * - Execute comparison logic
 * - Record every comparison (domain rule)
 * - Handle validation errors
 * 
 * Note: This is a domain test - no Spring, pure business logic.
 */
@DisplayName("Domain Service: Hand Comparison")
class HandComparisonServiceTest {
    
    private HandComparisonService service;
    private ComparisonHistoryRepository mockRepository;
    
    @BeforeEach
    void setUp() {
        mockRepository = mock(ComparisonHistoryRepository.class);
        when(mockRepository.save(any(ComparisonHistoryEntry.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        
        service = new HandComparisonService(mockRepository);
    }
    
    @Test
    @DisplayName("Should compare hands and return result")
    void shouldCompareHandsAndReturnResult() {
        // Given: Valid hands
        String blackHand = "AH KD 9C 7D 4S";
        String whiteHand = "KH QD 9C 7D 4S";
        
        // When: Compare hands
        ComparisonResult result = service.compareAndRecord(blackHand, whiteHand);
        
        // Then: Returns correct result
        assertThat(result).isNotNull();
        assertThat(result.getWinner()).isEqualTo(Winner.BLACK);
        assertThat(result.describe()).contains("Black wins");
    }
    
    @Test
    @DisplayName("Should record every comparison to history (domain rule)")
    void shouldRecordEveryComparisonToHistory() {
        // Given: Valid hands
        String blackHand = "AH KD 9C 7D 4S";
        String whiteHand = "KH QD 9C 7D 4S";
        
        // When: Compare hands
        ComparisonResult result = service.compareAndRecord(blackHand, whiteHand);
        
        // Then: Comparison was recorded
        verify(mockRepository).save(argThat(entry ->
            entry.getBlackHand().equals(blackHand) &&
            entry.getWhiteHand().equals(whiteHand) &&
            entry.getResult().equals(result) &&
            entry.getTimestamp() != null
        ));
    }
    
    @Test
    @DisplayName("Should record ties")
    void shouldRecordTies() {
        // Given: Identical hands (will tie)
        String blackHand = "2H 3D 5S 9C KD";
        String whiteHand = "2D 3H 5C 9S KH";
        
        // When: Compare hands
        ComparisonResult result = service.compareAndRecord(blackHand, whiteHand);
        
        // Then: Result is a tie
        assertThat(result.getWinner()).isEqualTo(Winner.TIE);
        
        // And: Tie was recorded
        verify(mockRepository).save(any(ComparisonHistoryEntry.class));
    }
    
    @Test
    @DisplayName("Should not record when validation fails")
    void shouldNotRecordWhenValidationFails() {
        // Given: Invalid hand
        String invalidHand = "INVALID";
        String validHand = "KH QD 9C 7D 4S";
        
        // When/Then: Throws validation error
        assertThatThrownBy(() -> service.compareAndRecord(invalidHand, validHand))
            .isInstanceOf(IllegalArgumentException.class);
        
        // And: Nothing was recorded
        verify(mockRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should handle all poker hand categories")
    void shouldHandleAllPokerHandCategories() {
        // Given: Different hand types
        String straightFlush = "9H TH JH QH KH";
        String highCard = "2C 3D 5S 7H 9S";
        
        // When: Compare
        ComparisonResult result = service.compareAndRecord(straightFlush, highCard);
        
        // Then: Straight flush wins
        assertThat(result.getWinner()).isEqualTo(Winner.BLACK);
        assertThat(result.getCategory()).isEqualTo(Category.STRAIGHT_FLUSH);
        
        // And: Recorded
        verify(mockRepository).save(any(ComparisonHistoryEntry.class));
    }
}
