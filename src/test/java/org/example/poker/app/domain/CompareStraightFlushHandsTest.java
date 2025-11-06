package org.example.poker.app.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Story 10: Detect Straight Flush")
class CompareStraightFlushHandsTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        // Straight Flush vs lower categories
        "'9H TH JH QH KH' | 'QH QD QS QC 2D' | BLACK | 'Black wins - straight flush'",
        "'AH 2H 3H 4H 5H' | '3H 3D 3S 9C 9D' | BLACK | 'Black wins - straight flush'",
        "'6D 7D 8D 9D TD' | 'AH QH 9H 5H 2H' | BLACK | 'Black wins - straight flush'",
        
        // Compare straight flushes by high card
        "'TH JH QH KH AH' | '9D TD JD QD KD' | BLACK | 'Black wins - straight flush: Ace'",
        "'9S TS JS QS KS' | '8H 9H TH JH QH' | BLACK | 'Black wins - straight flush: King'",
        "'6C 7C 8C 9C TC' | '5D 6D 7D 8D 9D' | BLACK | 'Black wins - straight flush: 10'",
        
        // A-low straight flush (steel wheel)
        "'AH 2H 3H 4H 5H' | '2D 3D 4D 5D 6D' | WHITE | 'White wins - straight flush: 6'",
        "'2S 3S 4S 5S 6S' | 'AS 2S 3S 4S 5S' | BLACK | 'Black wins - straight flush: 6'",
        
        // Royal flush vs lower straight flush
        "'TH JH QH KH AH' | '9H TH JH QH KH' | BLACK | 'Black wins - straight flush: Ace'",
        
        // Identical straight flushes - tie
        "'9H TH JH QH KH' | '9D TD JD QD KD' | TIE   | 'Tie'"
    })
    @DisplayName("Straight flush comparison scenarios")
    void straightFlushComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
        // Given
        Hand black = Hand.parse(blackHand);
        Hand white = Hand.parse(whiteHand);
        
        // When
        ComparisonResult result = black.compare(white);
        
        // Then
        assertThat(result.getWinner()).isEqualTo(expectedWinner);
        assertThat(result.describe()).contains(expectedDescription);
    }
}
