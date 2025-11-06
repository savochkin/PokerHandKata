package org.example.poker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Story 6: Detect Straight")
class CompareStraightHandsTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        // Straight vs lower categories
        "'9H 8D 7S 6C 5H' | '7H 7D 7S QD 2C' | BLACK | 'Black wins - straight'",
        "'5H 4D 3S 2C AH' | 'AH AD KC KD 3S' | BLACK | 'Black wins - straight'",
        "'6H 5D 4S 3C 2H' | '9H 9D AC 7D 4S' | BLACK | 'Black wins - straight'",
        
        // Compare straights by high card
        "'TH 9D 8S 7C 6H' | '9H 8D 7S 6C 5D' | BLACK | 'Black wins - straight: 10'",
        "'9H 8D 7S 6C 5D' | '8H 7D 6S 5C 4D' | BLACK | 'Black wins - straight: 9'",
        "'AH KD QS JC TH' | 'KH QD JS TC 9D' | BLACK | 'Black wins - straight: Ace'",
        
        // A-low straight (wheel)
        "'5H 4D 3S 2C AH' | '6H 5D 4S 3C 2D' | WHITE | 'White wins - straight: 6'",
        "'6H 5D 4S 3C 2D' | '5H 4D 3S 2C AH' | BLACK | 'Black wins - straight: 6'",
        
        // Identical straights - tie
        "'9H 8D 7S 6C 5H' | '9C 8H 7D 6S 5C' | TIE   | 'Tie'"
    })
    @DisplayName("Straight comparison scenarios")
    void straightComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
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
