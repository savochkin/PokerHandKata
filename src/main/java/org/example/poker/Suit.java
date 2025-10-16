package org.example.poker;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Suit {
    CLUBS('C'),
    DIAMONDS('D'),
    HEARTS('H'),
    SPADES('S');

    private final char symbol;

    public static Suit fromSymbol(char symbol) {
        for (Suit suit : values()) {
            if (suit.symbol == symbol) {
                return suit;
            }
        }
        // TODO: Task 2 - Handle invalid suit symbols
        // Should throw IllegalArgumentException with message containing "Invalid suit"
        return null;
    }
}
