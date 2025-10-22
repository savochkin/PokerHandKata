package org.example.poker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Story 1 - Task 2: Reject Wrong Input")
class RejectWrongInputsTest {

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
