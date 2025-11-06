package org.example.poker.adapters.in.cli;

import org.example.poker.app.port.in.CompareHandsUseCase;
import org.example.poker.app.domain.ComparisonResult;
import org.example.poker.app.domain.Winner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit test for CLI adapter.
 * Tests the adapter in isolation using a mock use case.
 * 
 * Focus: Verify CLI adapter correctly:
 * - Calls use case with provided arguments
 * - Formats domain result for CLI output
 * - Handles errors gracefully
 */
@DisplayName("CLI Adapter - Unit Tests")
class CliCompareHandsAdapterTest {
    
    private CompareHandsUseCase mockUseCase;
    private CliCompareHandsAdapter adapter;
    
    @BeforeEach
    void setUp() {
        mockUseCase = mock(CompareHandsUseCase.class);
        adapter = new CliCompareHandsAdapter(mockUseCase);
    }
    
    @Test
    @DisplayName("Should call use case and return formatted result")
    void shouldCallUseCaseAndReturnFormattedResult() {
        // Given: Mock use case returns a result
        ComparisonResult mockResult = ComparisonResult.builder()
                .winner(Winner.BLACK)
                .category(null)
                .winningRank(null)
                .losingRank(null)
                .build();
        when(mockUseCase.compareHands("AH KD 9C 7D 4S", "KH QD 9C 7D 4S"))
                .thenReturn(mockResult);
        
        // When: CLI command is executed
        String output = adapter.compareHands("AH KD 9C 7D 4S", "KH QD 9C 7D 4S");
        
        // Then: Use case was called and result formatted
        verify(mockUseCase).compareHands("AH KD 9C 7D 4S", "KH QD 9C 7D 4S");
        assertThat(output).isNotNull();
    }
    
    @ParameterizedTest
    @CsvSource({
        "INVALID, 'KH QD 9C 7D 4S', 'Invalid hand format'",
        "'AH KD 9C 7D 4S', INVALID, 'Invalid hand format'",
        "'AH KD 9C 7D', 'KH QD 9C 7D 4S', 'Hand must contain exactly 5 cards'"
    })
    @DisplayName("Should handle invalid input gracefully")
    void shouldHandleInvalidInputGracefully(String blackHand, String whiteHand, String expectedErrorPart) {
        // Given: Use case throws exception for invalid input
        when(mockUseCase.compareHands(blackHand, whiteHand))
                .thenThrow(new IllegalArgumentException(expectedErrorPart));
        
        // When: CLI command is executed with invalid input
        String output = adapter.compareHands(blackHand, whiteHand);
        
        // Then: Error message is returned
        assertThat(output).startsWith("Error:");
        assertThat(output).contains(expectedErrorPart);
    }
}
