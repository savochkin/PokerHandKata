package org.example.poker.app.port.in;

import lombok.Builder;
import lombok.Getter;

/**
 * Response DTO for hand comparison use case.
 * This is part of the port definition and represents the data contract
 * between the domain and the adapters.
 */
@Getter
@Builder
public class ComparisonResponse {
    private final String winner;
    private final String description;
}
