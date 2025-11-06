package org.example.poker.app.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Domain object representing a historical comparison entry.
 * 
 * This is a domain concept - the business cares about maintaining
 * an audit trail of all comparisons for analytics and compliance.
 * 
 * The ID is nullable until the entry is persisted by the repository.
 */
@Getter
@Builder(toBuilder = true)
public class ComparisonHistoryEntry {
    private final String id;
    private final String blackHand;
    private final String whiteHand;
    private final ComparisonResult result;
    private final LocalDateTime timestamp;
}
