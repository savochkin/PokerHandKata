package org.example.poker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

// TODO: Task 5 - Add @Builder annotation here
// This will make the code in Hand.compare() more readable
// Instead of: new ComparisonResult(Winner.BLACK, rank)
// You'll have: ComparisonResult.builder().winner(Winner.BLACK).winningRank(rank).build()
// Don't forget to also add @AllArgsConstructor (since @Builder needs a constructor)

@Builder
@Getter
public class ComparisonResult {
    private final Winner winner;
    private final Rank winningRank;
    private final Rank losingRank;
    
    public String describe() {
        if (winner == Winner.TIE) {
            return "Tie";
        }
        
        String winnerName = winner == Winner.BLACK ? "Black" : "White";
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
