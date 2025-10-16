package org.example.poker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Story 1: Parse & Validate Cards")
class Story1_ParseAndValidateCardsTest {

    @Nested
    @DisplayName("Acceptance Criterion: Parse well-formed hand strings")
    class ParseWellFormedHands {

        @Test
        @DisplayName("Given 'AH KD 3C TD 9S', when parsed, then returns 5 cards with correct ranks and suits")
        void givenWellFormedString_whenParsed_thenReturns5CardsWithCorrectRanksAndSuits() {
            // Given
            String handString = "AH KD 3C TD 9S";

            // When
            Hand hand = HandParser.parseHand(handString);

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
            Hand hand = HandParser.parseHand(handString);

            // Then
            assertThat(hand.getCards()).hasSize(5);
        }
    }

    @Nested
    @DisplayName("Acceptance Criterion: Reject duplicate cards")
    class RejectDuplicateCards {

        @ParameterizedTest
        @CsvSource({
                "AH AH 3C TD 9S, duplicate",
                "2H 2H 2H 3C 4D, duplicate",
                "KD KD QS JH TC, duplicate"
        })
        @DisplayName("Given duplicate cards, when parsing, then fails with validation error")
        void givenDuplicateCards_whenParsing_thenFailsWithValidationError(String handString, String expectedMessage) {
            // When / Then
            assertThatThrownBy(() -> HandParser.parseHand(handString))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(expectedMessage);
        }
    }

    @Nested
    @DisplayName("Acceptance Criterion: Reject malformed input")
    class RejectMalformedInput {

        @ParameterizedTest
        @CsvSource({
                "AX KD 3C TD 9S, Invalid suit",
                "AH KD ?? TD 9S, Invalid",
                "1H KD 3C TD 9S, Invalid rank",
                "XH KD 3C TD 9S, Invalid rank",
                "AH BH 3C TD 9S, Invalid rank"
        })
        @DisplayName("Given malformed tokens, when parsing, then fails with clear error message")
        void givenMalformedTokens_whenParsing_thenFailsWithClearErrorMessage(String handString, String expectedMessage) {
            // When / Then
            assertThatThrownBy(() -> HandParser.parseHand(handString))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(expectedMessage);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "AH KD 3C TD",
                "AH",
                "AH KD 3C TD 9S 2H"
        })
        @DisplayName("Given wrong number of cards, when parsing, then fails with validation error")
        void givenWrongNumberOfCards_whenParsing_thenFailsWithValidationError(String handString) {
            // When / Then
            assertThatThrownBy(() -> HandParser.parseHand(handString))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("5 cards");
        }

        @Test
        @DisplayName("Given empty string, when parsing, then fails with validation error")
        void givenEmptyString_whenParsing_thenFailsWithValidationError() {
            // When / Then
            assertThatThrownBy(() -> HandParser.parseHand(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("empty");
        }
    }
}
