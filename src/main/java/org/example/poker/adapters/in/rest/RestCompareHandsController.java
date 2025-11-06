package org.example.poker.adapters.in.rest;

import org.example.poker.app.port.in.CompareHandsUseCase;
import org.example.poker.app.port.in.ComparisonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter (inbound) for comparing poker hands.
 * This adapter translates HTTP requests into use case calls.
 * It depends on the inbound port (CompareHandsUseCase) but knows nothing about the domain implementation.
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
    public ResponseEntity<ComparisonResponse> compareHands(@RequestBody CompareHandsRequest request) {
        try {
            ComparisonResponse response = compareHandsUseCase.compareHands(request.getBlack(), request.getWhite());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Return 400 Bad Request for invalid hands
            return ResponseEntity.badRequest()
                    .body(ComparisonResponse.builder()
                            .winner("ERROR")
                            .description(e.getMessage())
                            .build());
        }
    }
}
