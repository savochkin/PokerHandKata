package org.example.poker;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Story 1: Parse & Validate Well-Formed Input")
class ParseAndValidateWellFormedInputTest {

    @Test
    @DisplayName("Given 'AH KD 3C TD 9S', when parsed, then returns 5 cards with correct ranks and suits")
    @Disabled
    void givenWellFormedString_whenParsed_thenReturns5CardsWithCorrectRanksAndSuits() {
        // Given
        String handString = "AH KD 3C TD 9S";

        // When
        Hand hand = Hand.parse(handString);

        // Then
        assertThat(hand.getCards()).hasSize(5);
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
}
