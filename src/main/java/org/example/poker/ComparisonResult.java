package org.example.poker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ComparisonResult {
    private final Winner winner;
    private final Rank winningRank;
    private final Rank losingRank;
    private final Category category;
    
    public String describe() {
        if (winner == Winner.TIE) {
            return "Tie";
        }
        
        String winnerName = winner == Winner.BLACK ? "Black" : "White";
        String categoryName = getCategoryName(category);
        
        if (winningRank == null) {
            return winnerName + " wins - " + categoryName;
        }
        
        String rankName = formatRankName(winningRank);
        return winnerName + " wins - " + categoryName + ": " + rankName;
    }
    
    private String getCategoryName(Category category) {
        return switch (category) {
            case HIGH_CARD -> "high card";
            case ONE_PAIR -> "pair";
            case TWO_PAIR -> "two pair";
            case THREE_OF_A_KIND -> "three of a kind";
            case STRAIGHT -> "straight";
            case FLUSH -> "flush";
            case FULL_HOUSE -> "full house";
            case FOUR_OF_A_KIND -> "four of a kind";
            case STRAIGHT_FLUSH -> "straight flush";
        };
    }
    
    private String formatRankName(Rank rank) {
        return switch (rank) {
            case TWO -> "2";
            case THREE -> "3";
            case FOUR -> "4";
            case FIVE -> "5";
            case SIX -> "6";
            case SEVEN -> "7";
            case EIGHT -> "8";
            case NINE -> "9";
            case TEN -> "10";
            case JACK -> "Jack";
            case QUEEN -> "Queen";
            case KING -> "King";
            case ACE -> "Ace";
        };
    }
}
