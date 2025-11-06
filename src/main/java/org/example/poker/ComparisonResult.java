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
        
        if (category == Category.ONE_PAIR) {
            if (winningRank == null) {
                return winnerName + " wins - pair";
            }
            String rankName = formatRankName(winningRank);
            return winnerName + " wins - pair: " + rankName;
        }
        
        // HIGH_CARD
        String rankName = formatRankName(winningRank);
        return winnerName + " wins - high card: " + rankName;
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
