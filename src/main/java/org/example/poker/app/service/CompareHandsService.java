package org.example.poker.app.service;

import org.example.poker.app.port.in.CompareHandsUseCase;
import org.example.poker.app.domain.ComparisonResult;
import org.example.poker.app.domain.Hand;
import org.springframework.stereotype.Service;

/**
 * Application service implementing the compare hands use case.
 * This service orchestrates the domain logic and is part of the application layer.
 * It depends only on the domain model (Hand) and implements the inbound port.
 * 
 * The service returns domain objects directly - no mapping to DTOs at this layer.
 * Adapters are responsible for translating domain objects to their specific formats.
 */
@Service
public class CompareHandsService implements CompareHandsUseCase {
    
    @Override
    public ComparisonResult compareHands(String blackHand, String whiteHand) {
        // Parse hands using domain model
        Hand black = Hand.parse(blackHand);
        Hand white = Hand.parse(whiteHand);
        
        // Execute and return domain logic result
        return black.compare(white);
    }
}
