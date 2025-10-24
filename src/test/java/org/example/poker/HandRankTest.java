package org.example.poker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HandRankTest {

    @ParameterizedTest
    @CsvSource({
            "'AH KD 9C 7D 4S', 'HIGH_CARD', 'A K 9 7 4'",
            "'2H 3D 5C 9S JD', 'HIGH_CARD', 'J 9 5 3 2'"
    })
    void shouldCompareHighCardHands(String handVal, Category category,
                                    String rankVal) {
        Hand hand = Hand.parse(handVal);

        // When
        HandRank rank = hand.rank();

        // Then
        assertThat(rank.getCategory()).isEqualTo(category);
        List<Rank> kickers = Arrays.stream(rankVal.split(" ")).map(String::toCharArray).map(v -> v[0]).map(Rank::fromSymbol).toList();
        assertThat(rank.getKickers()).containsExactly(kickers.toArray(new Rank[0]));
    }
}
