package org.example.poker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Story 8: Detect Full House")
class CompareFullHouseHandsTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        // Full House vs lower categories
        "'3H 3D 3S 9C 9D' | 'AH QH 9H 5H 2H' | BLACK | 'Black wins - full house'",
        "'2H 2D 2S 3C 3H' | '9H 8D 7S 6C 5H' | BLACK | 'Black wins - full house'",
        "'4H 4D 4S 2C 2H' | '7H 7D 7S QD 2C' | BLACK | 'Black wins - full house'",
        
        // Compare by trips rank (trips decide, not pair)
        "'TH TD TS 2C 2D' | '9H 9D 9S AC AD' | BLACK | 'Black wins - full house: 10'",
        "'9H 9D 9S AC AD' | '8H 8D 8S KC KD' | BLACK | 'Black wins - full house: 9'",
        "'AH AD AS KC KD' | 'KH KD KS QC QD' | BLACK | 'Black wins - full house: Ace'",
        
        // Same trips, compare by pair rank
        "'7H 7D 7S QC QD' | '7C 7S 7H JD JC' | BLACK | 'Black wins - full house: Queen'",
        "'5H 5D 5S AC AD' | '5C 5S 5H KD KC' | BLACK | 'Black wins - full house: Ace'",
        
        // Identical full houses - tie
        "'3H 3D 3S 9C 9D' | '3C 3S 3H 9S 9H' | TIE   | 'Tie'"
    })
    @DisplayName("Full house comparison scenarios")
    void fullHouseComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
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
