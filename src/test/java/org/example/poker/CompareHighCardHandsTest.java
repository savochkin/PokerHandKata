package org.example.poker;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CompareHighCardHandsTest {

    @Test
    void shouldReturnWhiteWhenWhiteWins() {
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
        Hand black = Hand.parse("AH KD 9C 7D 4S");
        Hand white = Hand.parse("AH KD 9C 7D 3S");

        ComparisonResult result = black.compare(white);

        assertThat(result.getWinningRank()).isEqualTo(Rank.FOUR);
        assertThat(result.getLosingRank()).isEqualTo(Rank.THREE); // This will fail!
    }

    // TODO: Task 4 - Refactor these tests into parameterized tests
    // Notice how many tests follow the same pattern?
    // Consider consolidating them using @ParameterizedTest and @CsvSource

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
