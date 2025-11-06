package org.example.poker.adapters.in.cli;

import org.example.poker.app.port.in.CompareHandsUseCase;
import org.example.poker.app.domain.ComparisonResult;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * CLI adapter (inbound) for comparing poker hands.
 * This adapter translates command-line input into use case calls.
 * 
 * Responsibilities:
 * - Parse CLI arguments
 * - Call use case (same port as REST adapter uses)
 * - Format domain result for CLI output
 * 
 * Note: This adapter and REST adapter work simultaneously,
 * both using the same CompareHandsUseCase port.
 */
@ShellComponent
public class CliCompareHandsAdapter {
    
    private final CompareHandsUseCase compareHandsUseCase;
    
    public CliCompareHandsAdapter(CompareHandsUseCase compareHandsUseCase) {
        this.compareHandsUseCase = compareHandsUseCase;
    }
    
    /**
     * Compare two poker hands via CLI.
     * 
     * Usage:
     *   shell:> compare "AH KD 9C 7D 4S" "KH QD 9C 7D 4S"
     *   Black wins - high card: Ace
     * 
     * @param blackHand Black player's hand (e.g., "AH KD 9C 7D 4S")
     * @param whiteHand White player's hand (e.g., "KH QD 9C 7D 4S")
     * @return Comparison result as string
     */
    @ShellMethod(key = "compare", value = "Compare two poker hands")
    public String compareHands(
            @ShellOption(help = "Black player's hand (e.g., 'AH KD 9C 7D 4S')") String blackHand,
            @ShellOption(help = "White player's hand (e.g., 'KH QD 9C 7D 4S')") String whiteHand) {
        
        try {
            // Call use case - returns domain object
            ComparisonResult result = compareHandsUseCase.compareHands(blackHand, whiteHand);
            
            // Format for CLI output (simple string)
            return result.describe();
            
        } catch (IllegalArgumentException e) {
            // Return error message for invalid input
            return "Error: " + e.getMessage();
        }
    }
}
