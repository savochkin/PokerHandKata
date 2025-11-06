package org.example.poker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Story 7: Detect Flush")
class CompareFlushHandsTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        // Flush vs lower categories
        "'AH QH 9H 5H 2H' | '9H 8D 7S 6C 5H' | BLACK | 'Black wins - flush'",
        "'2D 4D 6D 8D TD' | '7H 7D 7S QD 2C' | BLACK | 'Black wins - flush'",
        "'3S 5S 7S 9S JS' | 'AH AD KC KD 3S' | BLACK | 'Black wins - flush'",
        
        // Compare flushes by highest card
        "'AH KH 9H 5H 2H' | 'KD QD JD 9D 7D' | BLACK | 'Black wins - flush: Ace'",
        "'KS QS JS 9S 7S' | 'QH JH TH 8H 6H' | BLACK | 'Black wins - flush: King'",
        
        // Same high card, compare by next
        "'AH KH 9H 5H 2H' | 'AC QC JC TC 9C' | BLACK | 'Black wins - flush: King'",
        "'KH QH JH 9H 7H' | 'KD QD JD 9D 6D' | BLACK | 'Black wins - flush: 7'",
        
        // Identical flushes - tie
        "'AH KH 9H 5H 2H' | 'AD KD 9D 5D 2D' | TIE   | 'Tie'"
    })
    @DisplayName("Flush comparison scenarios")
    void flushComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
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
