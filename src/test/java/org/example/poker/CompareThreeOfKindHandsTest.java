package org.example.poker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Story 5: Detect Three of a Kind")
class CompareThreeOfKindHandsTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        // Three of a Kind vs lower categories
        "'7H 7D 7S QD 2C' | 'AH AD KC KD 3S' | BLACK | 'Black wins - three of a kind'",
        "'2H 2D 2S 3C 4H' | '9H 9D AC 7D 4S' | BLACK | 'Black wins - three of a kind'",
        "'3H 3D 3S 2C 4H' | 'AH KD QC JS 9S' | BLACK | 'Black wins - three of a kind'",
        
        // Compare by trips rank
        "'AH AD AS KC 2D' | 'KH KD KS QC 3D' | BLACK | 'Black wins - three of a kind: Ace'",
        "'KH KD KS QC 3D' | 'QH QD QS JC 2D' | BLACK | 'Black wins - three of a kind: King'",
        "'3H 3D 3S 2C 4H' | '2H 2D 2S AC KH' | BLACK | 'Black wins - three of a kind: 3'",
        
        // Same trips, compare by first kicker
        "'7H 7D 7S QD 2C' | '7C 7S 7H JD 3C' | BLACK | 'Black wins - three of a kind: Queen'",
        "'9H 9D 9S AC 2C' | '9C 9S 9H KD 3C' | BLACK | 'Black wins - three of a kind: Ace'",
        
        // Same trips and first kicker, compare by second kicker
        "'7H 7D 7S QD 5C' | '7C 7S 7H QC 4D' | BLACK | 'Black wins - three of a kind: 5'",
        "'5H 5D 5S KC 3C' | '5C 5S 5H KD 2D' | BLACK | 'Black wins - three of a kind: 3'",
        
        // Identical three of a kind - tie
        "'7H 7D 7S QD 2C' | '7C 7S 7H QC 2D' | TIE   | 'Tie'"
    })
    @DisplayName("Three of a kind comparison scenarios")
    void threeOfKindComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
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
