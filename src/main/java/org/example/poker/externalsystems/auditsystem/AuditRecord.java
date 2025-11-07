package org.example.poker.externalsystems.auditsystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * Audit record data model from the external audit system.
 * 
 * This is the EXTERNAL SYSTEM'S data model - not our domain model.
 * 
 * Key points:
 * - We don't control this structure
 * - It may have different field names than our domain
 * - It may have more/fewer fields than we need
 * - Our adapter must map between our domain and this model
 * 
 * In this kata, it happens to be similar to our domain model,
 * but in real scenarios, external systems often have very different models.
 */
@Value
public class AuditRecord {
    String id;
    String blackHand;
    String whiteHand;
    ComparisonOutcome outcome;
    LocalDateTime timestamp;
    
    @JsonCreator
    public AuditRecord(
            @JsonProperty("id") String id,
            @JsonProperty("blackHand") String blackHand,
            @JsonProperty("whiteHand") String whiteHand,
            @JsonProperty("outcome") ComparisonOutcome outcome,
            @JsonProperty("timestamp") LocalDateTime timestamp) {
        this.id = id;
        this.blackHand = blackHand;
        this.whiteHand = whiteHand;
        this.outcome = outcome;
        this.timestamp = timestamp;
    }
}
