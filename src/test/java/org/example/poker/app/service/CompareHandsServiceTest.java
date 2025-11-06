package org.example.poker.app.service;

import org.example.poker.app.port.in.ComparisonResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit test for CompareHandsService.
 * Tests the service layer in isolation (no Spring context needed).
 */
class CompareHandsServiceTest {
    
    private CompareHandsService service;
    
    @BeforeEach
    void setUp() {
        service = new CompareHandsService();
    }
    
    @Test
    void shouldCompareHighCardHands() {
        ComparisonResponse response = service.compareHands(
                "AH KD 9C 7D 4S",
                "KH QD 9C 7D 4S"
        );
        
        assertThat(response.getWinner()).isEqualTo("BLACK");
        assertThat(response.getDescription()).isEqualTo("Black wins - high card: Ace");
    }
    
    @Test
    void shouldReturnTieForEqualHands() {
        ComparisonResponse response = service.compareHands(
                "2H 3D 5S 9C KD",
                "2D 3H 5C 9S KH"
        );
        
        assertThat(response.getWinner()).isEqualTo("TIE");
        assertThat(response.getDescription()).isEqualTo("Tie");
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
        ComparisonResponse response = service.compareHands(
                "2H 2D 5S 9C KD",
                "3C 4H 5C 8C AH"
        );
        
        assertThat(response.getWinner()).isEqualTo("BLACK");
        assertThat(response.getDescription()).isEqualTo("Black wins - pair");
    }
}
