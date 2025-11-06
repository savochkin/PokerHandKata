package org.example.poker.adapters.in.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * REST API request DTO for comparing hands.
 * This is specific to the REST adapter and not part of the domain or port.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompareHandsRequest {
    private String black;
    private String white;
}
