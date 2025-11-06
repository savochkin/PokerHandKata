package org.example.poker.adapters.in.rest;

import org.example.poker.app.port.in.CompareHandsUseCase;
import org.example.poker.app.domain.ComparisonResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter (inbound) for comparing poker hands.
 * This adapter translates HTTP requests into use case calls and domain results into JSON responses.
 * It depends on the inbound port (CompareHandsUseCase) but knows nothing about the domain implementation.
 * 
 * Responsibilities:
 * - Translate HTTP requests to use case calls
 * - Translate domain objects (ComparisonResult) to REST DTOs (ComparisonResponse)
 * - Handle HTTP-specific concerns (status codes, error handling)
 */
@RestController
@RequestMapping("/api/poker")
public class RestCompareHandsController {
    
    private final CompareHandsUseCase compareHandsUseCase;
    
    public RestCompareHandsController(CompareHandsUseCase compareHandsUseCase) {
        this.compareHandsUseCase = compareHandsUseCase;
    }
    
    /**
     * Compare two poker hands via REST API.
     * 
     * Example request:
     * POST /api/poker/compare
     * {
     *   "black": "AH KD 9C 7D 4S",
     *   "white": "KH QD 9C 7D 4S"
     * }
     * 
     * Example response:
     * {
     *   "winner": "BLACK",
     *   "description": "Black wins - high card: Ace"
     * }
     */
    @PostMapping("/compare")
    public ResponseEntity<CompareHandsResponse> compareHands(@RequestBody CompareHandsRequest request) {
        try {
            // Call use case - returns domain object
            ComparisonResult result = compareHandsUseCase.compareHands(request.getBlack(), request.getWhite());
            
            // Map domain object to REST DTO
            CompareHandsResponse response = toResponse(result);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Return 400 Bad Request for invalid hands
            return ResponseEntity.badRequest()
                    .body(CompareHandsResponse.builder()
                            .winner("ERROR")
                            .description(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Maps domain ComparisonResult to REST CompareHandsResponse DTO.
     * This is the adapter's responsibility - translating domain to external format.
     */
    private CompareHandsResponse toResponse(ComparisonResult result) {
        return CompareHandsResponse.builder()
                .winner(result.getWinner().name())
                .description(result.describe())
                .build();
    }
}
