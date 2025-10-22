package org.example.poker;

// TODO: Task 3 - Refactor to use Lombok annotations
// Add: @Getter, @AllArgsConstructor
public enum Suit {
    CLUBS('C'),
    DIAMONDS('D'),
    HEARTS('H'),
    SPADES('S');

    private final char symbol;

    Suit(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static Suit fromSymbol(char symbol) {
        for (Suit suit : values()) {
            if (suit.symbol == symbol) {
                return suit;
            }
        }
        throw new IllegalArgumentException("Invalid suit symbol: " + symbol);
    }
}
