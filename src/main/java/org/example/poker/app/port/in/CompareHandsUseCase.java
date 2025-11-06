package org.example.poker.app.port.in;

/**
 * Inbound port for comparing poker hands.
 * This interface defines the use case from the domain perspective.
 * Adapters (REST, CLI, etc.) will use this port to interact with the domain.
 */
public interface CompareHandsUseCase {
    
    /**
     * Compare two poker hands and return the result.
     * 
     * @param blackHand String representation of black player's hand (e.g., "AH KD 9C 7D 4S")
     * @param whiteHand String representation of white player's hand (e.g., "KH QD 9C 7D 4S")
     * @return ComparisonResponse containing winner and description
     */
    ComparisonResponse compareHands(String blackHand, String whiteHand);
}
