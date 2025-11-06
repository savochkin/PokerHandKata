package org.example.poker;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class Hand {
    private final List<Card> cards;

    // Override Lombok's generated getter to return unmodifiable list
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    private void validate() {
        if (cards.size() != 5) {
            throw new IllegalArgumentException("A hand must contain exactly 5 cards, got " + cards.size());
        }
        
        // Check for duplicate cards by comparing HashSet size with original list size
        Set<Card> uniqueCards = new HashSet<>(cards);
        if (uniqueCards.size() != cards.size()) {
            throw new IllegalArgumentException("Hand contains duplicate cards");
        }
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

    public static Hand parse(String handString) {
        if (handString == null || handString.trim().isEmpty()) {
            throw new IllegalArgumentException("Hand string cannot be empty");
        }
        
        String[] tokens = handString.trim().split("\\s+");
        
        if (tokens.length != 5) {
            throw new IllegalArgumentException("A hand must contain exactly 5 cards, got " + tokens.length);
        }
        
        List<Card> cardList = new java.util.ArrayList<>();
        for (String token : tokens) {
            try {
                cardList.add(Card.parse(token));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid card token: " + token + " - " + e.getMessage(), e);
            }
        }
        
        Hand hand = Hand.builder().cards(cardList).build();
        hand.validate();
        return hand;
    }

    private HandRank rank() {
        // Story 2: Assume all hands are High Card
        // Sort cards by rank descending to get kickers
        List<Rank> kickers = cards.stream()
                .map(Card::getRank)
                .sorted(Comparator.comparingInt(Rank::getValue).reversed())
                .toList();
        
        return new HandRank(Category.HIGH_CARD, kickers);
    }

    public ComparisonResult compare(Hand other) {
        HandRank thisRank = this.rank();
        HandRank otherRank = other.rank();
        
        // Compare kickers lexicographically
        List<Rank> thisKickers = thisRank.kickers();
        List<Rank> otherKickers = otherRank.kickers();
        
        for (int i = 0; i < thisKickers.size(); i++) {
            int comparison = Integer.compare(
                thisKickers.get(i).getValue(),
                otherKickers.get(i).getValue()
            );
            
            if (comparison > 0) {
                return ComparisonResult.builder()
                        .winner(Winner.BLACK)
                        .winningRank(thisKickers.get(i))
                        .losingRank(otherKickers.get(i))
                        .build();
            } else if (comparison < 0) {
                return ComparisonResult.builder()
                        .winner(Winner.WHITE)
                        .winningRank(otherKickers.get(i))
                        .losingRank(thisKickers.get(i))
                        .build();
            }
        }
        
        return ComparisonResult.builder()
                .winner(Winner.TIE)
                .winningRank(null)
                .losingRank(null)
                .build();
    }
}
