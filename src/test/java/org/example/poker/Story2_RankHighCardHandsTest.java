package org.example.poker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Story 2: Rank High Card Hands")
class Story2_RankHighCardHandsTest {

    @Nested
    @DisplayName("Acceptance Criterion: Classify hand as High Card with kickers")
    class ClassifyHighCardHands {

        @Test
        @DisplayName("Given 'AH KD 9C 7D 4S', when ranked, then returns HighCard with kickers [A,K,9,7,4]")
        void givenHighCardHand_whenRanked_thenReturnsHighCardWithKickers() {
            // Given
            Hand hand = HandParser.parseHand("AH KD 9C 7D 4S");

            // When
            // HandRank rank = HandRanker.rank(hand);

            // Then
            // assertThat(rank.getCategory()).isEqualTo(Category.HIGH_CARD);
            // assertThat(rank.getKickers()).containsExactly(
            //     Rank.ACE, Rank.KING, Rank.NINE, Rank.SEVEN, Rank.FOUR
            // );
            
            // TODO: Implement HandRanker.rank() - Story 2
        }

        @Test
        @DisplayName("Given '2H 3D 5C 9S JD', when ranked, then returns HighCard with kickers [J,9,5,3,2]")
        void givenAnotherHighCardHand_whenRanked_thenReturnsHighCardWithSortedKickers() {
            // Given
            Hand hand = HandParser.parseHand("2H 3D 5C 9S JD");

            // When
            // HandRank rank = HandRanker.rank(hand);

            // Then
            // assertThat(rank.getCategory()).isEqualTo(Category.HIGH_CARD);
            // assertThat(rank.getKickers()).containsExactly(
            //     Rank.JACK, Rank.NINE, Rank.FIVE, Rank.THREE, Rank.TWO
            // );
            
            // TODO: Implement HandRanker.rank() - Story 2
        }
    }

    @Nested
    @DisplayName("Acceptance Criterion: Compare High Card hands lexicographically")
    class CompareHighCardHands {

        @Test
        @DisplayName("Given two High Card hands, when comparing, then higher top card wins")
        void givenTwoHighCardHands_whenComparing_thenHigherTopCardWins() {
            // Given
            Hand handA = HandParser.parseHand("AH KD 9C 7D 4S");
            Hand handB = HandParser.parseHand("KH QD 9C 7D 4S");

            // When
            // int result = HandComparator.compare(handA, handB);

            // Then
            // assertThat(result).isPositive(); // handA wins
            
            // TODO: Implement HandComparator.compare() - Story 2/3
        }
    }
}
