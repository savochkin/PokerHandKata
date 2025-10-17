package org.example.poker;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rank {
    TWO('2', 2),
    THREE('3', 3),
    FOUR('4', 4),
    FIVE('5', 5),
    SIX('6', 6),
    SEVEN('7', 7),
    EIGHT('8', 8),
    NINE('9', 9),
    TEN('T', 10),
    JACK('J', 11),
    QUEEN('Q', 12),
    KING('K', 13),
    ACE('A', 14);

    private final char symbol;
    private final int value;

    public static Rank fromSymbol(char symbol) {
        for (Rank rank : values()) {
            if (rank.symbol == symbol) {
                return rank;
            }
        }
        // TODO: Task 2 - Handle invalid rank symbols
        int flag = 0;
        for (Rank rank : values()) {
            if (rank.symbol == symbol) {
                flag =1;
                break;
            }
        }
        if(flag == 0)
            throw new IllegalArgumentException("invalid rank");
        return null;
    }
}
