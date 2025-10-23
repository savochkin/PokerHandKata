package org.example.poker;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CompareHighCardHandsTest {

    // TODO: Task 1 - Write a test for when White wins
    // Bug report: "When White wins, the system incorrectly reports that Black wins!"
    // OPTIONAL: First run tests with coverage - notice the "else if (comparison < 0)" branch in Hand.compare() is not covered!
    // Test case: Black: "2H 3D 5S 9C KD", White: "2C 3H 4S 8C AH"
    // Expected: White should win with Ace
    // Write the test first, see it fail, then fix the bug in Hand.compare() line ~104
    // Key lesson: Code coverage helps identify untested paths where bugs hide

    // TODO: Task 3 - Refactor these tests into parameterized tests
    // Notice how many tests follow the same pattern?
    // Consider consolidating them using @ParameterizedTest and @CsvSource

    // TODO: Task 4 - Write a test for losingRank bug
    // Bug report: "The losingRank in ComparisonResult is sometimes incorrect!"
    // Write a test that checks both getWinningRank() and getLosingRank()
    // Test case: Black "AH KD 9C 7D 4S" vs White "AH KD 9C 7D 3S"
    // Expected: winningRank=FOUR, losingRank=THREE
    // The test will fail, exposing the bug in Hand.compare()
    // After fixing the bug, refactor to use builder pattern to prevent similar bugs

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

    // TODO: Task 5 - Write tests for immutability
    // Bug report: "We're seeing incorrect comparison results! Sometimes a hand that should win is losing."
    // Investigation: Client code was modifying the cards list: hand.getCards().clear()
    // This breaks comparison logic because the hand becomes empty!
    // Your task: Write tests that try to modify the cards list (clear(), add(), remove())
    // Tests should expect UnsupportedOperationException
    // Then fix Hand.getCards() to return an unmodifiable list
    // Hint: Override Lombok's generated getter with Collections.unmodifiableList()
    // Key lesson: Protect internal state by returning unmodifiable collections!
}
