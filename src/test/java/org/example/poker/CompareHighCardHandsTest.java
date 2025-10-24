package org.example.poker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class CompareHighCardHandsTest {

    @ParameterizedTest
    @CsvSource({
            "'2H 3D 5S 9C KD', '2C 3H 4S 8C AH', WHITE, 'White wins - high card: Ace'",
            "'2H 3D 5S 9C KD', '2C 3H 4S 8C KH', BLACK, 'Black wins - high card: 9'"
    })
    void shouldCompareHighCardHands(String blackHand, String whiteHand,
                                    Winner expectedWinner, String expectedDescription) {
        Hand black = Hand.parse(blackHand);
        Hand white = Hand.parse(whiteHand);

        // When
        ComparisonResult result = black.compare(white);

        // Then
        assertThat(result.getWinner()).isEqualTo(expectedWinner);
        assertThat(result.describe()).isEqualTo(expectedDescription);

    }

    // TODO: Task 5 - Write tests for immutability
    // Bug report: "We're seeing incorrect comparison results! Sometimes a hand that should win is losing."
    // Investigation: Client code was modifying the cards list: hand.getCards().clear()
    // This breaks comparison logic because the hand becomes empty!
    // Your task: Write tests that try to modify the cards list (clear(), add(), remove())
    // Tests should expect UnsupportedOperationException
    // Then fix Hand.getCards() to return an unmodifiable list
    // Hint: Override Lombok's generated getter with Collections.unmodifiableList()
    // Key lesson: Protect internal state by returning unmodifiable collections!


    @Test
    void shouldReturnTieWhenAllKickersAreEqual() {
        // Given
        Hand black = Hand.parse("2H 3D 5S 9C KD");
        Hand white = Hand.parse("2D 3H 5C 9S KH");
        
        // When
        ComparisonResult result = black.compare(white);
        
        // Then
        assertThat(result.getWinner()).isEqualTo(Winner.TIE);
        assertThat(result.describe()).isEqualTo("Tie");
    }

    @Test
    void shouldCompareHighCardHandsByDifferentKicker() {
        // Given
        Hand black = Hand.parse("2H 3D 5S 9C KD");
        Hand white = Hand.parse("2C 3H 4S 8C KH");
        
        // When
        ComparisonResult result = black.compare(white);
        
        // Then
        assertThat(result.getWinner()).isEqualTo(Winner.BLACK);
        assertThat(result.describe()).isEqualTo("Black wins - high card: 9");
    }

    @Test
    void shouldCompareHighCardHandsByLastKickerWhenOthersAreEqual() {
        // Given
        Hand black = Hand.parse("AH KD 9C 7D 4S");
        Hand white = Hand.parse("AH KD 9C 7D 3S");
        
        // When
        ComparisonResult result = black.compare(white);
        
        // Then
        assertThat(result.getWinner()).isEqualTo(Winner.BLACK);
        assertThat(result.describe()).isEqualTo("Black wins - high card: 4");
    }

    @Test
    void shouldCompareHighCardHandsBySecondKickerWhenFirstIsEqual() {
        // Given
        Hand black = Hand.parse("AH KD 9C 7D 4S");
        Hand white = Hand.parse("AH QD 9C 7D 4S");
        
        // When
        ComparisonResult result = black.compare(white);
        
        // Then
        assertThat(result.getWinner()).isEqualTo(Winner.BLACK);
        assertThat(result.describe()).isEqualTo("Black wins - high card: King");
    }
}
