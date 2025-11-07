package org.example.poker.externalsystems.auditsystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

/**
 * Comparison outcome data model from the external audit system.
 * 
 * This is part of the external system's API.
 * Notice it uses different field names and structure than our domain.
 */
@Value
public class ComparisonOutcome {
    String winner;
    String winningRank;
    String losingRank;
    String category;
    
    @JsonCreator
    public ComparisonOutcome(
            @JsonProperty("winner") String winner,
            @JsonProperty("winningRank") String winningRank,
            @JsonProperty("losingRank") String losingRank,
            @JsonProperty("category") String category) {
        this.winner = winner;
        this.winningRank = winningRank;
        this.losingRank = losingRank;
        this.category = category;
    }
}
