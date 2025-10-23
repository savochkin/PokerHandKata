package org.example.poker;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CompareHighCardHandsTest {

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
    void shouldCompareHighCardHandsByHighestCard() {
        // Given
        Hand black = Hand.parse("2H 3D 5S 9C KD");
        Hand white = Hand.parse("2C 3H 4S 8C AH");
        
        // When
        ComparisonResult result = black.compare(white);
        
        // Then
        assertThat(result.getWinner()).isEqualTo(Winner.WHITE);
        assertThat(result.describe()).isEqualTo("White wins - high card: Ace");
    }

    @Test
    void shouldReturnTieWhenAllKickersAreEqual() {
        // Given
        Hand black = Hand.parse("2H 3D 5S 9C KD");
        Hand white = Hand.parse("2D 3H 5C 9S KH");
        
        // When
        ComparisonResult result = black.compare(white);
        
        // Then
        assertThat(result.getWinner()).isEqualTo(Winner.TIE);
        assertThat(result.describe()).isEqualTo("Tie");
    }

    @Test
    void shouldCompareHighCardHandsByDifferentKicker() {
        // Given
        Hand black = Hand.parse("2H 3D 5S 9C KD");
        Hand white = Hand.parse("2C 3H 4S 8C KH");
        
        // When
        ComparisonResult result = black.compare(white);
        
        // Then
        assertThat(result.getWinner()).isEqualTo(Winner.BLACK);
        assertThat(result.describe()).isEqualTo("Black wins - high card: 9");
    }

    @Test
    void shouldCompareHighCardHandsByLastKickerWhenOthersAreEqual() {
        // Given
        Hand black = Hand.parse("AH KD 9C 7D 4S");
        Hand white = Hand.parse("AH KD 9C 7D 3S");
        
        // When
        ComparisonResult result = black.compare(white);
        
        // Then
        assertThat(result.getWinner()).isEqualTo(Winner.BLACK);
        assertThat(result.describe()).isEqualTo("Black wins - high card: 4");
    }

    @Test
    void shouldCompareHighCardHandsBySecondKickerWhenFirstIsEqual() {
        // Given
        Hand black = Hand.parse("AH KD 9C 7D 4S");
        Hand white = Hand.parse("AH QD 9C 7D 4S");
        
        // When
        ComparisonResult result = black.compare(white);
        
        // Then
        assertThat(result.getWinner()).isEqualTo(Winner.BLACK);
        assertThat(result.describe()).isEqualTo("Black wins - high card: King");
    }
}
