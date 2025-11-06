package org.example.poker.adapters.in.rest;

import org.example.poker.app.domain.ComparisonHistoryEntry;
import org.example.poker.app.domain.ComparisonResult;
import org.example.poker.app.domain.Winner;
import org.example.poker.app.port.in.GetComparisonHistoryUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for HistoryController.
 * Tests the REST adapter with Spring context.
 */
@WebMvcTest(HistoryController.class)
@DisplayName("HistoryController - REST Adapter Tests")
class HistoryControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private GetComparisonHistoryUseCase getHistoryUseCase;
    
    @Test
    @DisplayName("GET /api/poker/history should return all history entries")
    void shouldReturnAllHistoryEntries() throws Exception {
        // Given: Use case returns some history
        ComparisonResult result1 = ComparisonResult.builder()
                .winner(Winner.BLACK)
                .category(org.example.poker.app.domain.Category.HIGH_CARD)
                .build();
        ComparisonResult result2 = ComparisonResult.builder()
                .winner(Winner.WHITE)
                .category(org.example.poker.app.domain.Category.ONE_PAIR)
                .build();
        
        LocalDateTime timestamp1 = LocalDateTime.of(2025, 11, 6, 12, 0);
        LocalDateTime timestamp2 = LocalDateTime.of(2025, 11, 6, 12, 5);
        
        ComparisonHistoryEntry entry1 = ComparisonHistoryEntry.builder()
                .id("id1")
                .blackHand("AH KD 9C 7D 4S")
                .whiteHand("KH QD 9C 7D 4S")
                .result(result1)
                .timestamp(timestamp1)
                .build();
        
        ComparisonHistoryEntry entry2 = ComparisonHistoryEntry.builder()
                .id("id2")
                .blackHand("2H 3D 5S 9C KD")
                .whiteHand("2D 3H 5C 9S AH")
                .result(result2)
                .timestamp(timestamp2)
                .build();
        
        when(getHistoryUseCase.getHistory()).thenReturn(List.of(entry1, entry2));
        
        // When/Then: GET request returns history as JSON
        mockMvc.perform(get("/api/poker/history"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("id1"))
                .andExpect(jsonPath("$[0].blackHand").value("AH KD 9C 7D 4S"))
                .andExpect(jsonPath("$[0].whiteHand").value("KH QD 9C 7D 4S"))
                .andExpect(jsonPath("$[0].winner").value("BLACK"))
                .andExpect(jsonPath("$[1].id").value("id2"))
                .andExpect(jsonPath("$[1].winner").value("WHITE"));
    }
    
    @Test
    @DisplayName("GET /api/poker/history should return empty array when no history")
    void shouldReturnEmptyArrayWhenNoHistory() throws Exception {
        // Given: Use case returns empty list
        when(getHistoryUseCase.getHistory()).thenReturn(List.of());
        
        // When/Then: GET request returns empty array
        mockMvc.perform(get("/api/poker/history"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
