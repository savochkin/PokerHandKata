package org.example.poker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Task 5: Immutability Tests")
class ImmutabilityTest {

    @Test
    @DisplayName("Given a Hand, when attempting to clear cards list, then throws UnsupportedOperationException")
    void givenHand_whenAttemptingToClearCardsList_thenThrowsUnsupportedOperationException() {
        // Given
        Hand hand = Hand.parse("AH KD 3C TD 9S");

        // When / Then
        assertThatThrownBy(() -> hand.getCards().clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Given a Hand, when attempting to add card to list, then throws UnsupportedOperationException")
    void givenHand_whenAttemptingToAddCardToList_thenThrowsUnsupportedOperationException() {
        // Given
        Hand hand = Hand.parse("AH KD 3C TD 9S");
        Card newCard = new Card(Rank.TWO, Suit.HEARTS);

        // When / Then
        assertThatThrownBy(() -> hand.getCards().add(newCard))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Given a Hand, when attempting to remove card from list, then throws UnsupportedOperationException")
    void givenHand_whenAttemptingToRemoveCardFromList_thenThrowsUnsupportedOperationException() {
        // Given
        Hand hand = Hand.parse("AH KD 3C TD 9S");

        // When / Then
        assertThatThrownBy(() -> hand.getCards().remove(0))
                .isInstanceOf(UnsupportedOperationException.class);
    }

}
