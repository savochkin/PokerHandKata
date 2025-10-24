package org.example.poker;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandRankTest {

    @Test
    void shouldRankHighCardHandWithKickersSortedDescending() {
        // Given
        Hand hand = Hand.parse("AH KD 9C 7D 4S");
        
        // When
        HandRank rank = hand.rank();
        
        // Then
        assertThat(rank.getCategory()).isEqualTo(Category.HIGH_CARD);
        assertThat(rank.getKickers()).containsExactly(
            Rank.ACE, Rank.KING, Rank.NINE, Rank.SEVEN, Rank.FOUR
        );
    }

    @Test
    void shouldRankAnotherHighCardHandWithKickersSortedDescending() {
        // Given
        Hand hand = Hand.parse("2H 3D 5C 9S JD");
        
        // When
        HandRank rank = hand.rank();
        
        // Then
        assertThat(rank.getCategory()).isEqualTo(Category.HIGH_CARD);
        assertThat(rank.getKickers()).containsExactly(
            Rank.JACK, Rank.NINE, Rank.FIVE, Rank.THREE, Rank.TWO
        );
    }

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
}
