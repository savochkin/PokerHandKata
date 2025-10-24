package org.example.poker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CompareHighCardHandsTest {

    // TODO: Task 1 - Write a test for when White wins
    // Bug report: "When White wins, the system incorrectly reports that Black wins!"
    // OPTIONAL: First run tests with coverage - notice the "else if (comparison < 0)" branch in Hand.compare() is not covered!
    // Test case: Black: "2H 3D 5S 9C KD", White: "2C 3H 4S 8C AH"
    // Expected: White should win with Ace
    // Write the test first, see it fail, then fix the bug in Hand.compare() line ~104
    // Key lesson: Code coverage helps identify untested paths where bugs hide


    // TODO: Task 5 - Write tests for immutability
    // Bug report: "We're seeing incorrect comparison results! Sometimes a hand that should win is losing."
    // Investigation: Client code was modifying the cards list: hand.getCards().clear()
    // This breaks comparison logic because the hand becomes empty!
    // Your task: Write tests that try to modify the cards list (clear(), add(), remove())
    // Tests should expect UnsupportedOperationException
    // Then fix Hand.getCards() to return an unmodifiable list
    // Hint: Override Lombok's generated getter with Collections.unmodifiableList()
    //
    @Test
    void shouldReturnUnmodifiableCardsList() {
        Hand hand = Hand.parse("AH KD 9C 7D 4S");

        assertThatThrownBy(() -> hand.getCards().clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldNotAllowAddingCards() {
        Hand hand = Hand.parse("AH KD 9C 7D 4S");
        Card newCard = new Card(Rank.TWO, Suit.HEARTS);

        assertThatThrownBy(() -> hand.getCards().add(newCard))
                .isInstanceOf(UnsupportedOperationException.class);
    }


    @ParameterizedTest
    @CsvSource({
            "'2H 3D 5S 9C KD', '2C 3H 4S 8C AH', WHITE, 'White wins - high card: Ace'",
            "'2H 3D 5S 9C KD', '2C 3H 4S 8C KH', BLACK, 'Black wins - high card: 9'",
            "'AH KD 9C 7D 4S', 'AH KD 9C 7D 3S', BLACK, 'Black wins - high card: 4'",
            "'AH KD 9C 7D 4S', 'AH QD 9C 7D 4S', BLACK, 'Black wins - high card: King'",
            "'AH KD 9C 7D 4S', 'AH KD 9C 7D 4S', TIE, 'Tie'",
            "'2H 3D 5S 9C KD', '2D 3H 5C 9S KH', TIE, 'Tie'"
    })
    void shouldCompareHighCardHands(
            String blackHand,
            String whiteHand,
            Winner expectedWinner,
            String expectedDescription
    ) {
        // Test implementation
        Hand black = Hand.parse(blackHand);
        Hand white = Hand.parse(whiteHand);

        ComparisonResult result = black.compare(white);

        assertThat(result.getWinner()).isEqualTo(expectedWinner);
        assertThat(result.describe()).isEqualTo(expectedDescription);
    }
}
