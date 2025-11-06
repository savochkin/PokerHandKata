package org.example.poker.app.service;

import org.example.poker.app.domain.ComparisonResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit test for CompareHandsService.
 * Tests the service layer orchestration (no Spring context needed).
 * 
 * Focus: Verify that the service correctly orchestrates domain logic.
 * - Parses hand strings and delegates to domain
 * - Returns domain objects (ComparisonResult)
 * - Propagates domain exceptions
 * 
 * Note: Business rules (comparison logic) are tested in domain tests.
 */
@DisplayName("CompareHandsService - Orchestration Tests")
class CompareHandsServiceTest {
    
    private CompareHandsService service;
    
    @BeforeEach
    void setUp() {
        service = new CompareHandsService();
    }
    
    @Test
    @DisplayName("Should delegate to domain and return ComparisonResult")
    void shouldDelegateToDomainAndReturnComparisonResult() {
        // Given: Valid hand strings
        String blackHand = "AH KD 9C 7D 4S";
        String whiteHand = "KH QD 9C 7D 4S";
        
        // When: Service orchestrates comparison
        ComparisonResult result = service.compareHands(blackHand, whiteHand);
        
        // Then: Returns domain object with comparison result
        assertThat(result).isNotNull();
        assertThat(result.getWinner()).isNotNull();
        assertThat(result.describe()).isNotEmpty();
    }
    
    @ParameterizedTest
    @CsvSource({
        "INVALID,     'KH QD 9C 7D 4S'",  // Invalid black hand
        "'AH KD 9C 7D 4S', INVALID",       // Invalid white hand
        "'AH KD 9C 7D',    'KH QD 9C 7D 4S'",  // Wrong number of cards
        "'AH AH 9C 7D 4S', 'KH QD 9C 7D 4S'"   // Duplicate card
    })
    @DisplayName("Should propagate domain validation errors")
    void shouldPropagateDomainValidationErrors(String blackHand, String whiteHand) {
        // When/Then: Service propagates domain exceptions
        assertThatThrownBy(() -> service.compareHands(blackHand, whiteHand))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
