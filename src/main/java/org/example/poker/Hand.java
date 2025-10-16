package org.example.poker;

import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class Hand {
    private final List<Card> cards;

    public Hand(List<Card> cards) {
        if (cards.size() != 5) {
            throw new IllegalArgumentException("A hand must contain exactly 5 cards, got " + cards.size());
        }
        
        // Check for duplicates
        Set<Card> uniqueCards = new HashSet<>(cards);
        if (uniqueCards.size() != 5) {
            throw new IllegalArgumentException("Hand contains duplicate cards");
        }
        
        this.cards = List.copyOf(cards);
    }

    public boolean containsCard(Rank rank, Suit suit) {
        Card searchCard = new Card(rank, suit);
        return cards.contains(searchCard);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            if (i > 0) sb.append(" ");
            sb.append(cards.get(i));
        }
        return sb.toString();
    }
}
