package org.example.poker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(strings = {
            "2H 3D 5S 9C KD",
            "AH KD 3C TD 9S",
            "AS KS QS JS TS",
            "2C 2D 2H 2S 3C"
    })
    @DisplayName("Given various valid hand formats, when parsed, then successfully creates hand")
    void givenVariousValidFormats_whenParsed_thenSuccessfullyCreatesHand(String handString) {
        // When
        Hand hand = Hand.parse(handString);

        // Then
        assertThat(hand.getCards()).hasSize(5);
    }
}
