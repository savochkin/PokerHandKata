package org.example.poker.app.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Story 1: Parse & Validate Hands")
class ParseAndValidateTest {

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
    @DisplayName("Given valid hand string, when parsed, then successfully creates hand with 5 cards")
    void givenValidHandString_whenParsed_thenSuccessfullyCreatesHandWith5Cards(String handString) {
        // When
        Hand hand = Hand.parse(handString);

        // Then
        assertThat(hand.getCards()).hasSize(5);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "",                     // empty string
            "AH KD 3C TD",          // wrong number: 4 cards
            "AH",                   // wrong number: 1 card
            "AH KD 3C TD 9S 2H",    // wrong number: 6 cards
            "AH AH 3C TD 9S",       // duplicate card
            "2H 2H 2H 3C 4D",       // duplicate card (multiple)
            "KD KD QS JH TC",       // duplicate card
            "1H KD 3C TD 9S",       // invalid rank: 1
            "XH KD 3C TD 9S",       // invalid rank: X
            "BH KD 3C TD 9S",       // invalid rank: B
            "AX KD 3C TD 9S",       // invalid suit: X
            "AZ KD 3C TD 9S",       // invalid suit: Z
            "AB KD 3C TD 9S",       // invalid suit: B
            "AH KD ?? TD 9S",       // invalid card format: ??
            "AH KD 3C TD 9",        // invalid card format: single char
            "A KD 3C TD 9S"         // invalid card format: single char
    })
    @DisplayName("Given invalid hand input, when parsing, then fails with validation error")
    void givenInvalidHandInput_whenParsing_thenFailsWithValidationError(String handString) {
        // When / Then
        assertThatThrownBy(() -> Hand.parse(handString))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
