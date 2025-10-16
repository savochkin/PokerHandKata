package org.example.poker;

import java.util.Objects;

// TODO: Task 3 - Refactor to use Lombok annotations
// Remove boilerplate code and add: @Getter, @Builder, @EqualsAndHashCode
public class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }

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

        return new Card(Rank.fromSymbol(rankChar), Suit.fromSymbol(suitChar));
    }
}
