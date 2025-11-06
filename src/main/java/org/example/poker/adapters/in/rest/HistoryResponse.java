package org.example.poker.adapters.in.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * REST DTO for comparison history response.
 * 
 * This is an adapter-specific data structure for HTTP responses.
 * It maps from domain objects (ComparisonHistoryEntry) to REST format.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponse {
    private String id;
    private String blackHand;
    private String whiteHand;
    private String winner;
    private String description;
    private LocalDateTime timestamp;
}
