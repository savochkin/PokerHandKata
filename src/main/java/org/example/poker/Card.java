package org.example.poker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class Card {
    private final Rank rank;
    private final Suit suit;

    @Override
    public String toString() {
        return "" + rank.getSymbol() + suit.getSymbol();
    }

    public static Card parse(String cardString) {
        if (cardString == null || cardString.length() != 2) {
            throw new IllegalArgumentException("Invalid card format: " + cardString);
        }
        
        char rankChar = cardString.charAt(0);
        char suitChar = cardString.charAt(1);

        return Card.builder()
                .rank(Rank.fromSymbol(rankChar))
                .suit(Suit.fromSymbol(suitChar))
                .build();
    }
}
