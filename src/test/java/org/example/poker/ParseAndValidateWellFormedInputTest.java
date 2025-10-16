package org.example.poker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Story 1: Parse & Validate Well-Formed Input")
class ParseAndValidateWellFormedInputTest {

    @Test
    @DisplayName("Given 'AH KD 3C TD 9S', when parsed, then returns 5 cards with correct ranks and suits")
    void givenWellFormedString_whenParsed_thenReturns5CardsWithCorrectRanksAndSuits() {
        // Given
        String handString = "AH KD 3C TD 9S";

        // When
        Hand hand = Hand.parse(handString);

        // Then
        assertThat(hand.getCards()).hasSize(5);
        assertThat(hand.containsCard(Rank.ACE, Suit.HEARTS)).isTrue();
        assertThat(hand.containsCard(Rank.KING, Suit.DIAMONDS)).isTrue();
        assertThat(hand.containsCard(Rank.THREE, Suit.CLUBS)).isTrue();
        assertThat(hand.containsCard(Rank.TEN, Suit.DIAMONDS)).isTrue();
        assertThat(hand.containsCard(Rank.NINE, Suit.SPADES)).isTrue();
    }

    // TODO: Task 4 - Refactor these individual tests into a single parameterized test
    // Hint: Use @ParameterizedTest with @ValueSource(strings = {...})
    // Hint: Look at RejectWrongInputsTest for examples of parameterized tests
    
    @Test
    @DisplayName("Given '2H 3D 5S 9C KD', when parsed, then successfully creates hand")
    void givenValidHand1_whenParsed_thenSuccessfullyCreatesHand() {
        // When
        Hand hand = Hand.parse("2H 3D 5S 9C KD");

        // Then
        assertThat(hand.getCards()).hasSize(5);
    }

    @Test
    @DisplayName("Given 'AH KD 3C TD 9S', when parsed, then successfully creates hand")
    void givenValidHand2_whenParsed_thenSuccessfullyCreatesHand() {
        // When
        Hand hand = Hand.parse("AH KD 3C TD 9S");

        // Then
        assertThat(hand.getCards()).hasSize(5);
    }

    @Test
    @DisplayName("Given 'AS KS QS JS TS', when parsed, then successfully creates hand")
    void givenValidHand3_whenParsed_thenSuccessfullyCreatesHand() {
        // When
        Hand hand = Hand.parse("AS KS QS JS TS");

        // Then
        assertThat(hand.getCards()).hasSize(5);
    }

    @Test
    @DisplayName("Given '2C 2D 2H 2S 3C', when parsed, then successfully creates hand")
    void givenValidHand4_whenParsed_thenSuccessfullyCreatesHand() {
        // When
        Hand hand = Hand.parse("2C 2D 2H 2S 3C");

        // Then
        assertThat(hand.getCards()).hasSize(5);
    }
}
