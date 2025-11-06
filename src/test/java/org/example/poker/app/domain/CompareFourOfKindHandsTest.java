package org.example.poker.app.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Story 9: Detect Four of a Kind")
class CompareFourOfKindHandsTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        // Four of a Kind vs lower categories
        "'QH QD QS QC 2D' | '3H 3D 3S 9C 9D' | BLACK | 'Black wins - four of a kind'",
        "'2H 2D 2S 2C 3H' | 'AH QH 9H 5H 2H' | BLACK | 'Black wins - four of a kind'",
        "'5H 5D 5S 5C AH' | '9H 8D 7S 6C 5H' | BLACK | 'Black wins - four of a kind'",
        
        // Compare by quads rank
        "'AH AD AS AC 2D' | 'KH KD KS KC 3D' | BLACK | 'Black wins - four of a kind: Ace'",
        "'KH KD KS KC 3D' | 'QH QD QS QC AD' | BLACK | 'Black wins - four of a kind: King'",
        "'3H 3D 3S 3C 2D' | '2H 2D 2S 2C AH' | BLACK | 'Black wins - four of a kind: 3'",
        
        // Same quads, compare by kicker
        "'7H 7D 7S 7C QD' | '7C 7S 7H 7D JC' | BLACK | 'Black wins - four of a kind: Queen'",
        "'9H 9D 9S 9C AC' | '9C 9S 9H 9D KD' | BLACK | 'Black wins - four of a kind: Ace'",
        
        // Identical four of a kind - tie
        "'QH QD QS QC 2D' | 'QC QS QH QD 2H' | TIE   | 'Tie'"
    })
    @DisplayName("Four of a kind comparison scenarios")
    void fourOfKindComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
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
