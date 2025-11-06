package org.example.poker.app.service;

import org.example.poker.app.domain.ComparisonResult;
import org.example.poker.app.domain.Winner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit test for CompareHandsService.
 * Tests the service layer in isolation (no Spring context needed).
 * Service now returns domain objects (ComparisonResult) directly.
 */
class CompareHandsServiceTest {
    
    private CompareHandsService service;
    
    @BeforeEach
    void setUp() {
        service = new CompareHandsService();
    }
    
    @Test
    void shouldCompareHighCardHands() {
        ComparisonResult result = service.compareHands(
                "AH KD 9C 7D 4S",
                "KH QD 9C 7D 4S"
        );
        
        assertThat(result.getWinner()).isEqualTo(Winner.BLACK);
        assertThat(result.describe()).isEqualTo("Black wins - high card: Ace");
    }
    
    @Test
    void shouldReturnTieForEqualHands() {
        ComparisonResult result = service.compareHands(
                "2H 3D 5S 9C KD",
                "2D 3H 5C 9S KH"
        );
        
        assertThat(result.getWinner()).isEqualTo(Winner.TIE);
        assertThat(result.describe()).isEqualTo("Tie");
    }
    
    @Test
    void shouldThrowExceptionForInvalidBlackHand() {
        assertThatThrownBy(() -> service.compareHands(
                "INVALID",
                "KH QD 9C 7D 4S"
        )).isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    void shouldThrowExceptionForInvalidWhiteHand() {
        assertThatThrownBy(() -> service.compareHands(
                "AH KD 9C 7D 4S",
                "INVALID"
        )).isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    void shouldComparePairVsHighCard() {
        ComparisonResult result = service.compareHands(
                "2H 2D 5S 9C KD",
                "3C 4H 5C 8C AH"
        );
        
        assertThat(result.getWinner()).isEqualTo(Winner.BLACK);
        assertThat(result.describe()).isEqualTo("Black wins - pair");
    }
}
