package org.example.poker;

import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        // Count occurrences of each rank
        var rankCounts = cards.stream()
                .map(Card::getRank)
                .collect(Collectors.groupingBy(r -> r, Collectors.counting()));
        
        // Get sorted ranks
        List<Rank> sortedRanks = cards.stream()
                .map(Card::getRank)
                .sorted(Comparator.comparingInt(Rank::getValue).reversed())
                .toList();
        
        // Check for flush
        boolean isFlush = cards.stream()
                .map(Card::getSuit)
                .distinct()
                .count() == 1;
        
        // Check for straight
        boolean isStraight = isStraight(sortedRanks);
        int straightHigh = isStraight ? getStraightHigh(sortedRanks) : 0;
        
        // STRAIGHT_FLUSH
        if (isFlush && isStraight) {
            return new HandRank(Category.STRAIGHT_FLUSH, List.of(Rank.fromValue(straightHigh)));
        }
        
        // FOUR_OF_A_KIND
        var quads = rankCounts.entrySet().stream()
                .filter(e -> e.getValue() == 4)
                .map(Map.Entry::getKey)
                .findFirst();
        
        if (quads.isPresent()) {
            Rank quadRank = quads.get();
            Rank kicker = sortedRanks.stream()
                    .filter(r -> !r.equals(quadRank))
                    .findFirst()
                    .orElseThrow();
            return new HandRank(Category.FOUR_OF_A_KIND, List.of(quadRank, kicker));
        }
        
        // FULL_HOUSE (3 + 2)
        var trips = rankCounts.entrySet().stream()
                .filter(e -> e.getValue() == 3)
                .map(Map.Entry::getKey)
                .findFirst();
        
        var pairs = rankCounts.entrySet().stream()
                .filter(e -> e.getValue() == 2)
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparingInt(Rank::getValue).reversed())
                .toList();
        
        if (trips.isPresent() && !pairs.isEmpty()) {
            return new HandRank(Category.FULL_HOUSE, List.of(trips.get(), pairs.getFirst()));
        }
        
        // FLUSH
        if (isFlush) {
            return new HandRank(Category.FLUSH, sortedRanks);
        }
        
        // STRAIGHT
        if (isStraight) {
            return new HandRank(Category.STRAIGHT, List.of(Rank.fromValue(straightHigh)));
        }
        
        // THREE_OF_A_KIND
        if (trips.isPresent()) {
            Rank tripRank = trips.get();
            List<Rank> kickers = sortedRanks.stream()
                    .filter(r -> !r.equals(tripRank))
                    .toList();
            List<Rank> allRanks = new java.util.ArrayList<>();
            allRanks.add(tripRank);
            allRanks.addAll(kickers);
            return new HandRank(Category.THREE_OF_A_KIND, allRanks);
        }
        
        // TWO_PAIR
        if (pairs.size() == 2) {
            Rank highPair = pairs.get(0);
            Rank lowPair = pairs.get(1);
            Rank kicker = sortedRanks.stream()
                    .filter(r -> !r.equals(highPair) && !r.equals(lowPair))
                    .findFirst()
                    .orElseThrow();
            return new HandRank(Category.TWO_PAIR, List.of(highPair, lowPair, kicker));
        }
        
        // ONE_PAIR
        if (pairs.size() == 1) {
            Rank pairRank = pairs.getFirst();
            List<Rank> kickers = sortedRanks.stream()
                    .filter(r -> !r.equals(pairRank))
                    .toList();
            List<Rank> allRanks = new java.util.ArrayList<>();
            allRanks.add(pairRank);
            allRanks.addAll(kickers);
            return new HandRank(Category.ONE_PAIR, allRanks);
        }
        
        // HIGH_CARD
        return new HandRank(Category.HIGH_CARD, sortedRanks);
    }
    
    private boolean isStraight(List<Rank> sortedRanks) {
        // Check regular straight
        for (int i = 0; i < sortedRanks.size() - 1; i++) {
            if (sortedRanks.get(i).getValue() - sortedRanks.get(i + 1).getValue() != 1) {
                // Check for A-low straight (A 2 3 4 5)
                return isWheelStraight(sortedRanks);
            }
        }
        return true;
    }
    
    private boolean isWheelStraight(List<Rank> sortedRanks) {
        // A-low straight: A 5 4 3 2 (sorted descending)
        return sortedRanks.size() == 5 &&
                sortedRanks.get(0) == Rank.ACE &&
                sortedRanks.get(1) == Rank.FIVE &&
                sortedRanks.get(2) == Rank.FOUR &&
                sortedRanks.get(3) == Rank.THREE &&
                sortedRanks.get(4) == Rank.TWO;
    }
    
    private int getStraightHigh(List<Rank> sortedRanks) {
        // For A-low straight, high card is 5
        if (isWheelStraight(sortedRanks)) {
            return Rank.FIVE.getValue();
        }
        // Otherwise, highest card
        return sortedRanks.getFirst().getValue();
    }

    public ComparisonResult compare(Hand other) {
        HandRank thisRank = this.rank();
        HandRank otherRank = other.rank();
        
        // Compare by category first
        int categoryComparison = Integer.compare(
                thisRank.category().getStrength(),
                otherRank.category().getStrength()
        );
        
        if (categoryComparison > 0) {
            return ComparisonResult.builder()
                    .winner(Winner.BLACK)
                    .winningRank(null)
                    .losingRank(null)
                    .category(thisRank.category())
                    .build();
        } else if (categoryComparison < 0) {
            return ComparisonResult.builder()
                    .winner(Winner.WHITE)
                    .winningRank(null)
                    .losingRank(null)
                    .category(otherRank.category())
                    .build();
        }
        
        // Same category - compare kickers lexicographically
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
                        .category(thisRank.category())
                        .build();
            } else if (comparison < 0) {
                return ComparisonResult.builder()
                        .winner(Winner.WHITE)
                        .winningRank(otherKickers.get(i))
                        .losingRank(thisKickers.get(i))
                        .category(thisRank.category())
                        .build();
            }
        }
        
        return ComparisonResult.builder()
                .winner(Winner.TIE)
                .winningRank(null)
                .losingRank(null)
                .category(thisRank.category())
                .build();
    }
}
