package org.example.poker;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
}
