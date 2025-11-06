package org.example.poker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CompareHighCardHandsTest {

    @Test
    void shouldReportWhiteWinsWhenWhiteHasHigherCard() {
        // Given
        Hand black = Hand.parse("2H 3D 5S 9C KD");
        Hand white = Hand.parse("2C 3H 4S 8C AH");
        
        // When
        ComparisonResult result = black.compare(white);
        
        // Then
        assertThat(result.getWinner()).isEqualTo(Winner.WHITE);
        assertThat(result.describe()).isEqualTo("White wins - high card: Ace");
    }

    @Test
    void shouldSetCorrectLosingRankWhenBlackWins() {
        // Given
        Hand black = Hand.parse("AH KD 9C 7D 4S");
        Hand white = Hand.parse("AH KD 9C 7D 3S");
        
        // When
        ComparisonResult result = black.compare(white);
        
        // Then
        assertThat(result.getWinner()).isEqualTo(Winner.BLACK);
        assertThat(result.getWinningRank()).isEqualTo(Rank.FOUR);
        assertThat(result.getLosingRank()).isEqualTo(Rank.THREE);
    }

    @ParameterizedTest
    @CsvSource({
            "'2H 3D 5S 9C KD', '2D 3H 5C 9S KH', TIE, 'Tie'",
            "'2H 3D 5S 9C KD', '2C 3H 4S 8C KH', BLACK, 'Black wins - high card: 9'",
            "'AH KD 9C 7D 4S', 'AH KD 9C 7D 3S', BLACK, 'Black wins - high card: 4'",
            "'AH KD 9C 7D 4S', 'AH QD 9C 7D 4S', BLACK, 'Black wins - high card: King'"
    })
    void shouldCompareHighCardHands(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
        // Given
        Hand black = Hand.parse(blackHand);
        Hand white = Hand.parse(whiteHand);
        
        // When
        ComparisonResult result = black.compare(white);
        
        // Then
        assertThat(result.getWinner()).isEqualTo(expectedWinner);
        assertThat(result.describe()).isEqualTo(expectedDescription);
    }

    @Test
    void shouldReturnUnmodifiableCardsList() {
        // Given
        Hand hand = Hand.parse("AH KD 9C 7D 4S");
        
        // When/Then
        assertThatThrownBy(() -> hand.getCards().clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldNotAllowAddingCards() {
        // Given
        Hand hand = Hand.parse("AH KD 9C 7D 4S");
        Card newCard = new Card(Rank.TWO, Suit.HEARTS);
        
        // When/Then
        assertThatThrownBy(() -> hand.getCards().add(newCard))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldNotAllowRemovingCards() {
        // Given
        Hand hand = Hand.parse("AH KD 9C 7D 4S");
        
        // When/Then
        assertThatThrownBy(() -> hand.getCards().removeFirst())
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
