package org.example.poker.app.service;

import org.example.poker.app.port.in.CompareHandsUseCase;
import org.example.poker.app.port.in.ComparisonResponse;
import org.example.poker.app.domain.ComparisonResult;
import org.example.poker.app.domain.Hand;
import org.springframework.stereotype.Service;

/**
 * Application service implementing the compare hands use case.
 * This service orchestrates the domain logic and is part of the application layer.
 * It depends only on the domain model (Hand) and implements the inbound port.
 */
@Service
public class CompareHandsService implements CompareHandsUseCase {
    
    @Override
    public ComparisonResponse compareHands(String blackHand, String whiteHand) {
        // Parse hands using domain model
        Hand black = Hand.parse(blackHand);
        Hand white = Hand.parse(whiteHand);
        
        // Execute domain logic
        ComparisonResult result = black.compare(white);
        
        // Map domain result to port response DTO
        return ComparisonResponse.builder()
                .winner(result.getWinner().name())
                .description(result.describe())
                .build();
    }
}
