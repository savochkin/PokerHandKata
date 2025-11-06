package org.example.poker.app.port.in;

import org.example.poker.app.domain.ComparisonHistoryEntry;
import org.example.poker.app.domain.ComparisonResult;
import org.example.poker.app.port.out.ComparisonHistoryRepository;
import org.example.poker.app.service.CompareHandsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

/**
 * Tests for the CompareHands use case orchestration.
 * 
 * This test suite verifies use case responsibilities:
 * - Saves comparisons to history
 * - Handles validation errors
 * 
 * Note: Comparison logic (which hand wins) is tested in HandComparisonTest (domain tests).
 */
@DisplayName("Use Case: Compare Poker Hands - Orchestration")
class CompareHandsUseCaseTest {
    
    private CompareHandsUseCase useCase;
    private ComparisonHistoryRepository mockRepository;
    
    @BeforeEach
    void setUp() {
        mockRepository = mock(ComparisonHistoryRepository.class);
        when(mockRepository.save(any(ComparisonHistoryEntry.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        
        useCase = new CompareHandsService(mockRepository);
    }
    
    @Test
    @DisplayName("Should delegate to domain and return comparison result")
    void shouldDelegateToDomainAndReturnComparisonResult() {
        // Given: Valid hands
        String blackHand = "AH KD 9C 7D 4S";
        String whiteHand = "KH QD 9C 7D 4S";
        
        // When: Use case compares hands
        ComparisonResult result = useCase.compareHands(blackHand, whiteHand);
        
        // Then: Returns a valid comparison result
        assertThat(result).isNotNull();
        assertThat(result.getWinner()).isNotNull();
        assertThat(result.describe()).isNotEmpty();
    }
    
    @Test
    @DisplayName("Should save every comparison to history")
    void shouldSaveEveryComparisonToHistory() {
        // Given: Valid hands
        String blackHand = "AH KD 9C 7D 4S";
        String whiteHand = "KH QD 9C 7D 4S";
        
        // When: Use case compares hands
        ComparisonResult result = useCase.compareHands(blackHand, whiteHand);
        
        // Then: Comparison was saved to history
        verify(mockRepository).save(argThat(entry ->
            entry.getBlackHand().equals(blackHand) &&
            entry.getWhiteHand().equals(whiteHand) &&
            entry.getResult().equals(result) &&
            entry.getTimestamp() != null &&
            entry.getId() == null
        ));
    }
    
    @Test
    @DisplayName("Should save even when hands result in a tie")
    void shouldSaveEvenWhenHandsResultInTie() {
        // Given: Identical hands (will tie)
        String blackHand = "2H 3D 5S 9C KD";
        String whiteHand = "2D 3H 5C 9S KH";
        
        // When: Use case compares hands
        useCase.compareHands(blackHand, whiteHand);
        
        // Then: Comparison was saved (even for ties)
        verify(mockRepository).save(any(ComparisonHistoryEntry.class));
    }
    
    @Test
    @DisplayName("Should not save when validation fails")
    void shouldNotSaveWhenValidationFails() {
        // Given: Invalid hand
        String invalidHand = "INVALID";
        String validHand = "KH QD 9C 7D 4S";
        
        // When/Then: Use case throws exception
        assertThatThrownBy(() -> useCase.compareHands(invalidHand, validHand))
            .isInstanceOf(IllegalArgumentException.class);
        
        // And: Nothing was saved to history
        verify(mockRepository, never()).save(any());
    }
}
