package org.example.poker.adapters.in.rest;

import lombok.Builder;
import lombok.Getter;

/**
 * REST API response DTO for hand comparison.
 * This is an adapter-level DTO that translates domain objects (ComparisonResult)
 * into JSON format for the REST API.
 * 
 * This DTO is specific to the REST adapter and should not be used by other adapters
 * (CLI, GraphQL, etc.) - each adapter defines its own output format.
 */
@Getter
@Builder
public class CompareHandsResponse {
    private final String winner;
    private final String description;
}
