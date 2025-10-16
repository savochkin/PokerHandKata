package org.example.poker;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
public class Hand {
    @Singular
    private final List<Card> cards;

    private void validate() {
        if (cards.size() != 5) {
            throw new IllegalArgumentException("A hand must contain exactly 5 cards, got " + cards.size());
        }
        
        // TODO: Story 1 - Implement duplicate card validation
        // Hint: A hand should not contain the same card twice
        // Expected error message should contain "duplicate"
    }

    public boolean containsCard(Rank rank, Suit suit) {
        Card searchCard = Card.builder().rank(rank).suit(suit).build();
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

    public static Hand parse(String handString) {
        if (handString == null || handString.trim().isEmpty()) {
            throw new IllegalArgumentException("Hand string cannot be empty");
        }
        
        String[] tokens = handString.trim().split("\\s+");
        
        if (tokens.length != 5) {
            throw new IllegalArgumentException("A hand must contain exactly 5 cards, got " + tokens.length);
        }
        
        HandBuilder builder = Hand.builder();
        for (String token : tokens) {
            try {
                builder.card(Card.parse(token));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid card token: " + token + " - " + e.getMessage(), e);
            }
        }
        
        Hand hand = builder.build();
        hand.validate();
        return hand;
    }
}
