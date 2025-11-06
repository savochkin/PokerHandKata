package org.example.poker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Story 4: Detect Two Pair")
class CompareTwoPairHandsTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        // Two Pair vs One Pair - two pair must win
        "'AH AD KC KD 3S' | '9H 9D AC 7D 4S' | BLACK | 'Black wins - two pair'",
        "'2H 2D 3C 3S 4H' | 'AH AD KC QD JS' | BLACK | 'Black wins - two pair'",
        
        // Two Pair vs High Card - two pair must win
        "'2H 2D 3C 3S 4H' | 'AH KD QC JS 9S' | BLACK | 'Black wins - two pair'",
        
        // Compare by higher pair first
        "'AH AD KC KD 3S' | 'QH QD JC JD 2S' | BLACK | 'Black wins - two pair: Ace'",
        "'KH KD QC QD 2S' | 'JH JD TC TD AS' | BLACK | 'Black wins - two pair: King'",
        "'3H 3D 2C 2S 4H' | '5H 5D 4C 4S 3C' | WHITE | 'White wins - two pair: 5'",
        
        // Same higher pair, compare by lower pair
        "'AH AD KC KD 3S' | 'AC AS QD QS 2H' | BLACK | 'Black wins - two pair: King'",
        "'9H 9D 8C 8S 2H' | '9C 9S 7D 7H AH' | BLACK | 'Black wins - two pair: 8'",
        "'5H 5D 3C 3S 2H' | '5C 5S 4D 4H AH' | WHITE | 'White wins - two pair: 4'",
        
        // Same two pairs, compare by kicker
        "'AH AD KC KD 3S' | 'AC AS KH KS 2D' | BLACK | 'Black wins - two pair: 3'",
        "'9H 9D 8C 8S 7H' | '9C 9S 8D 8H 6C' | BLACK | 'Black wins - two pair: 7'",
        "'2H 2D 3C 3S 5H' | '2C 2S 3D 3H 6C' | WHITE | 'White wins - two pair: 6'",
        
        // Identical two pairs - tie
        "'AH AD KC KD 3S' | 'AC AS KH KS 3D' | TIE   | 'Tie'"
    })
    @DisplayName("Two pair comparison scenarios")
    void twoPairComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
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
