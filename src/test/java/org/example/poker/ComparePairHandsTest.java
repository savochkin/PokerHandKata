package org.example.poker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Story 3: Detect One Pair")
class ComparePairHandsTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "'2H 2D 3C 4S 5H' | 'AH KD QC JS 9S' | BLACK | 'Black wins - pair'",
        "'AH AD 9C 7D 4S' | 'KH QD JC 8S 3C' | BLACK | 'Black wins - pair'",
        "'AH KC QD 8S 3C' | 'TS TD 8C 6D 4S' | WHITE | 'White wins - pair'",
        "'AH AD 9C 7D 4S' | 'KH KD QS JD 2C' | BLACK | 'Black wins - pair: Ace'",
        "'9H 9D AC 7D 4S' | 'TS TD 8C 6H 3S' | WHITE | 'White wins - pair: 10'",
        "'KH KD QS JD 2C' | '2H 2D AC KS QD' | BLACK | 'Black wins - pair: King'",
        "'9H 9D AC 7D 3S' | '9C 9S QD 7H 2C' | BLACK | 'Black wins - pair: Ace'",
        "'9H 9D AD 8D 3S' | '9C 9S AH 7H 3C' | BLACK | 'Black wins - pair: 8'",
        "'9H 9D AC KD 4S' | '9C 9S AD KH 3C' | BLACK | 'Black wins - pair: 4'",
        "'2H 2D AH KD 3S' | '2C 2S AH KD 4C' | WHITE | 'White wins - pair: 4'",
        "'AH AD KS QD JC' | 'AS AC KH QH 9C' | BLACK | 'Black wins - pair: Jack'",
        "'9H 9D AC KD 4S' | '9C 9S AD KH 4C' | TIE   | 'Tie'"
    })
    @DisplayName("Pair comparison scenarios")
    void pairComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
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
