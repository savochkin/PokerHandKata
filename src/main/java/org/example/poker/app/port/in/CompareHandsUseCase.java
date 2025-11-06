package org.example.poker.app.port.in;

import org.example.poker.app.domain.ComparisonResult;

/**
 * Inbound port for comparing poker hands.
 * This interface defines the use case from the domain perspective.
 * Adapters (REST, CLI, etc.) will use this port to interact with the domain.
 * 
 * The port returns domain objects (ComparisonResult) - adapters are responsible
 * for translating these into their specific formats (JSON, CLI output, etc.).
 */
public interface CompareHandsUseCase {
    
    /**
     * Compare two poker hands and return the result.
     * 
     * @param blackHand String representation of black player's hand (e.g., "AH KD 9C 7D 4S")
     * @param whiteHand String representation of white player's hand (e.g., "KH QD 9C 7D 4S")
     * @return ComparisonResult domain object containing winner and description
     */
    ComparisonResult compareHands(String blackHand, String whiteHand);
}
