package org.example.poker.adapters.in.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for REST adapter.
 * Tests the complete flow: REST request → Controller → Service → Domain → Response
 */
@SpringBootTest
@AutoConfigureMockMvc
class RestCompareHandsControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void shouldCompareHandsViaRestApi() throws Exception {
        String requestBody = """
                {
                    "black": "AH KD 9C 7D 4S",
                    "white": "KH QD 9C 7D 4S"
                }
                """;
        
        mockMvc.perform(post("/api/poker/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winner").value("BLACK"))
                .andExpect(jsonPath("$.description").value("Black wins - high card: Ace"));
    }
    
    @Test
    void shouldReturnWhiteWinsWhenWhiteHasHigherCard() throws Exception {
        String requestBody = """
                {
                    "black": "2H 3D 5S 9C KD",
                    "white": "2C 3H 4S 8C AH"
                }
                """;
        
        mockMvc.perform(post("/api/poker/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winner").value("WHITE"))
                .andExpect(jsonPath("$.description").value("White wins - high card: Ace"));
    }
    
    @Test
    void shouldReturnTieWhenHandsAreEqual() throws Exception {
        String requestBody = """
                {
                    "black": "2H 3D 5S 9C KD",
                    "white": "2D 3H 5C 9S KH"
                }
                """;
        
        mockMvc.perform(post("/api/poker/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winner").value("TIE"))
                .andExpect(jsonPath("$.description").value("Tie"));
    }
    
    @Test
    void shouldReturnBadRequestForInvalidHand() throws Exception {
        String requestBody = """
                {
                    "black": "INVALID HAND",
                    "white": "KH QD 9C 7D 4S"
                }
                """;
        
        mockMvc.perform(post("/api/poker/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.winner").value("ERROR"));
    }
    
    @Test
    void shouldComparePairVsHighCard() throws Exception {
        String requestBody = """
                {
                    "black": "2H 2D 5S 9C KD",
                    "white": "3C 4H 5C 8C AH"
                }
                """;
        
        mockMvc.perform(post("/api/poker/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winner").value("BLACK"))
                .andExpect(jsonPath("$.description").value("Black wins - pair"));
    }
    
    @Test
    void shouldCompareFullHouseVsFlush() throws Exception {
        String requestBody = """
                {
                    "black": "2H 4S 4C 2D 4H",
                    "white": "2S 8S AS QS 3S"
                }
                """;
        
        mockMvc.perform(post("/api/poker/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winner").value("BLACK"))
                .andExpect(jsonPath("$.description").value("Black wins - full house"));
    }
}
