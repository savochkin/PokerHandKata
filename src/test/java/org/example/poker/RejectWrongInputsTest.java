package org.example.poker;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Story 1 - Task 2: Reject Wrong Input")
class RejectWrongInputsTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "AH KD 3C TD",
            "AH",
            "AH KD 3C TD 9S 2H"
    })
    @DisplayName("Given wrong number of cards, when parsing, then fails with validation error")
    void givenWrongNumberOfCards_whenParsing_thenFailsWithValidationError(String handString) {
        // When / Then
        assertThatThrownBy(() -> Hand.parse(handString))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("5 cards");
    }

    @Test
    @DisplayName("Given empty string, when parsing, then fails with validation error")
    void givenEmptyString_whenParsing_thenFailsWithValidationError() {
        // When / Then
        assertThatThrownBy(() -> Hand.parse(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("empty");
    }

    // TODO: Task 1 - Implement duplicate card validation
    // These tests are currently FAILING - implement the validation logic to make them pass
    @ParameterizedTest
    @CsvSource({
            "AH AH 3C TD 9S, duplicate",
            "2H 2H 2H 3C 4D, duplicate",
            "KD KD QS JH TC, duplicate"
    })
    @DisplayName("Given duplicate cards, when parsing, then fails with validation error")
    void givenDuplicateCards_whenParsing_thenFailsWithValidationError(String handString, String expectedMessage) {
        // When / Then
        assertThatThrownBy(() -> Hand.parse(handString))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }

    // TODO: Task 2 - Write tests for invalid rank symbols
    //
    // Acceptance Criteria:
    // - Given invalid rank symbol (e.g., '1', 'X', 'B'), when parsing card, then throw IllegalArgumentException
    // - Error message should contain "Invalid rank"
    //
    // Examples to test:
    // - "1H" should fail (1 is not a valid rank)
    // - "XH" should fail (X is not a valid rank)
    // - "BH" should fail (B is not a valid rank)
    //
    // Hint: Use @ParameterizedTest with @ValueSource
    // Hint: Look at Task1_RejectDuplicateCardsTest for examples
    //
    // Write your test here:


    // TODO: Task 2 - Write tests for invalid suit symbols
    //
    // Acceptance Criteria:
    // - Given invalid suit symbol (e.g., 'X', 'Z', 'B'), when parsing card, then throw IllegalArgumentException
    // - Error message should contain "Invalid suit"
    //
    // Examples to test:
    // - "AX" should fail (X is not a valid suit)
    // - "AZ" should fail (Z is not a valid suit)
    // - "AB" should fail (B is not a valid suit)
    //
    // Hint: Use @ParameterizedTest with @ValueSource
    //
    // Write your test here:


    // TODO: Task 2 - Write tests for hands containing invalid cards
    //
    // Acceptance Criteria:
    // - Given hand string with invalid rank, when parsing, then throw IllegalArgumentException
    // - Given hand string with invalid suit, when parsing, then throw IllegalArgumentException
    // - Error message should indicate which token is invalid
    //
    // Examples to test:
    // - "AX KD 3C TD 9S" should fail (AX has invalid suit)
    // - "1H KD 3C TD 9S" should fail (1H has invalid rank)
    // - "AH KD ?? TD 9S" should fail (?? is completely invalid)
    //
    // Hint: Test Hand.parse() method
    // Hint: Error message should contain "Invalid"
    //
    // Write your test here:

}
