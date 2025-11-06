package org.example.poker.app.port.in;

import org.example.poker.app.domain.ComparisonResult;
import org.example.poker.app.domain.Winner;
import org.example.poker.app.service.CompareHandsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the CompareHands use case.
 * 
 * This test suite verifies the complete "Compare Poker Hands" use case
 * by testing through the port (CompareHandsUseCase interface).
 * 
 * Organization: Tests are grouped by poker hand category (Story/Feature)
 * following the original kata structure.
 * 
 * Philosophy: Test via use case entry point, not domain internals.
 * This allows easy refactoring of domain model while keeping tests stable.
 */
@DisplayName("Use Case: Compare Poker Hands")
class CompareHandsUseCaseTest {
    
    private CompareHandsUseCase useCase;
    
    @BeforeEach
    void setUp() {
        useCase = new CompareHandsService();
    }
    
    @Nested
    @DisplayName("Story 1: Parse & Validate Hands")
    class ParseAndValidateScenarios {
        
        @ParameterizedTest
        @ValueSource(strings = {
            "2H 3D 5S 9C KD",
            "AH KD 3C TD 9S",
            "AS KS QS JS TS",
            "2C 2D 2H 2S 3C"
        })
        @DisplayName("Should successfully parse valid hand strings")
        void shouldSuccessfullyParseValidHandStrings(String validHand) {
            // When: Use case called with valid hands
            ComparisonResult result = useCase.compareHands(validHand, "2H 3D 5S 9C KD");
            
            // Then: Returns valid result
            assertThat(result).isNotNull();
            assertThat(result.getWinner()).isNotNull();
        }
        
        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {
            "",                     // empty string
            "AH KD 3C TD",          // wrong number: 4 cards
            "AH",                   // wrong number: 1 card
            "AH KD 3C TD 9S 2H",    // wrong number: 6 cards
            "AH AH 3C TD 9S",       // duplicate card
            "2H 2H 2H 3C 4D",       // duplicate card (multiple)
            "KD KD QS JH TC",       // duplicate card
            "1H KD 3C TD 9S",       // invalid rank: 1
            "XH KD 3C TD 9S",       // invalid rank: X
            "BH KD 3C TD 9S",       // invalid rank: B
            "AX KD 3C TD 9S",       // invalid suit: X
            "AZ KD 3C TD 9S",       // invalid suit: Z
            "AB KD 3C TD 9S",       // invalid suit: B
            "AH KD ?? TD 9S",       // invalid card format: ??
            "AH KD 3C TD 9",        // invalid card format: single char
            "A KD 3C TD 9S"         // invalid card format: single char
        })
        @DisplayName("Should reject invalid hand input")
        void shouldRejectInvalidHandInput(String invalidHand) {
            // When/Then: Use case rejects invalid input
            assertThatThrownBy(() -> useCase.compareHands(invalidHand, "2H 3D 5S 9C KD"))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
    
    @Nested
    @DisplayName("Story 2: High Card Comparison")
    class HighCardScenarios {
        
        @ParameterizedTest
        @CsvSource(delimiter = '|', value = {
            "'2H 3D 5S 9C KD' | '2D 3H 5C 9S KH' | TIE   | 'Tie'",
            "'2H 3D 5S 9C KD' | '2C 3H 4S 8C KH' | BLACK | 'Black wins - high card: 9'",
            "'AH KD 9C 7D 4S' | 'AH KD 9C 7D 3S' | BLACK | 'Black wins - high card: 4'",
            "'AH KD 9C 7D 4S' | 'AH QD 9C 7D 4S' | BLACK | 'Black wins - high card: King'",
            "'2H 3D 5S 9C KD' | '2C 3H 4S 8C AH' | WHITE | 'White wins - high card: Ace'"
        })
        @DisplayName("High card comparison scenarios")
        void highCardComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
            // When
            ComparisonResult result = useCase.compareHands(blackHand, whiteHand);
            
            // Then
            assertThat(result.getWinner()).isEqualTo(expectedWinner);
            assertThat(result.describe()).isEqualTo(expectedDescription);
        }
    }
    
    @Nested
    @DisplayName("Story 3: One Pair")
    class PairScenarios {
        
        @ParameterizedTest
        @CsvSource(delimiter = '|', value = {
            "'2H 2D 3C 4S 5H' | 'AH KD QC JS 9S' | BLACK | 'Black wins - pair'",
            "'AH AD 9C 7D 4S' | 'KH QD JC 8S 3C' | BLACK | 'Black wins - pair'",
            "'AH KC QD 8S 3C' | 'TS TD 8C 6D 4S' | WHITE | 'White wins - pair'",
            "'AH AD 9C 7D 4S' | 'KH KD QS JD 2C' | BLACK | 'Black wins - pair: Ace'",
            "'9H 9D AC 7D 4S' | 'TS TD 8C 6H 3S' | WHITE | 'White wins - pair: 10'",
            "'KH KD QS JD 2C' | '2H 2D AC KS QD' | BLACK | 'Black wins - pair: King'",
            "'9H 9D AC 7D 3S' | '9C 9S QD 7H 2C' | BLACK | 'Black wins - pair: Ace'",
            "'9H 9D AD 8D 3S' | '9C 9S AH 7H 3C' | BLACK | 'Black wins - pair: 8'",
            "'9H 9D AC KD 4S' | '9C 9S AD KH 3C' | BLACK | 'Black wins - pair: 4'",
            "'2H 2D AH KD 3S' | '2C 2S AH KD 4C' | WHITE | 'White wins - pair: 4'",
            "'AH AD KS QD JC' | 'AS AC KH QH 9C' | BLACK | 'Black wins - pair: Jack'",
            "'9H 9D AC KD 4S' | '9C 9S AD KH 4C' | TIE   | 'Tie'"
        })
        @DisplayName("Pair comparison scenarios")
        void pairComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
            // When
            ComparisonResult result = useCase.compareHands(blackHand, whiteHand);
            
            // Then
            assertThat(result.getWinner()).isEqualTo(expectedWinner);
            assertThat(result.describe()).contains(expectedDescription);
        }
    }
    
    @Nested
    @DisplayName("Story 4: Two Pair")
    class TwoPairScenarios {
        
        @ParameterizedTest
        @CsvSource(delimiter = '|', value = {
            // Two Pair vs One Pair - two pair must win
            "'AH AD KC KD 3S' | '9H 9D AC 7D 4S' | BLACK | 'Black wins - two pair'",
            "'2H 2D 3C 3S 4H' | 'AH AD KC QD JS' | BLACK | 'Black wins - two pair'",
            
            // Two Pair vs High Card - two pair must win
            "'2H 2D 3C 3S 4H' | 'AH KD QC JS 9S' | BLACK | 'Black wins - two pair'",
            
            // Compare by higher pair first
            "'AH AD KC KD 3S' | 'QH QD JC JD 2S' | BLACK | 'Black wins - two pair: Ace'",
            "'KH KD QC QD 2S' | 'JH JD TC TD AS' | BLACK | 'Black wins - two pair: King'",
            "'3H 3D 2C 2S 4H' | '5H 5D 4C 4S 3C' | WHITE | 'White wins - two pair: 5'",
            
            // Same higher pair, compare by lower pair
            "'AH AD KC KD 3S' | 'AC AS QD QS 2H' | BLACK | 'Black wins - two pair: King'",
            "'9H 9D 8C 8S 2H' | '9C 9S 7D 7H AH' | BLACK | 'Black wins - two pair: 8'",
            "'5H 5D 3C 3S 2H' | '5C 5S 4D 4H AH' | WHITE | 'White wins - two pair: 4'",
            
            // Same two pairs, compare by kicker
            "'AH AD KC KD 3S' | 'AC AS KH KS 2D' | BLACK | 'Black wins - two pair: 3'",
            "'9H 9D 8C 8S 7H' | '9C 9S 8D 8H 6C' | BLACK | 'Black wins - two pair: 7'",
            "'2H 2D 3C 3S 5H' | '2C 2S 3D 3H 6C' | WHITE | 'White wins - two pair: 6'",
            
            // Identical two pairs - tie
            "'AH AD KC KD 3S' | 'AC AS KH KS 3D' | TIE   | 'Tie'"
        })
        @DisplayName("Two pair comparison scenarios")
        void twoPairComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
            // When
            ComparisonResult result = useCase.compareHands(blackHand, whiteHand);
            
            // Then
            assertThat(result.getWinner()).isEqualTo(expectedWinner);
            assertThat(result.describe()).contains(expectedDescription);
        }
    }
    
    @Nested
    @DisplayName("Story 5: Three of a Kind")
    class ThreeOfKindScenarios {
        
        @ParameterizedTest
        @CsvSource(delimiter = '|', value = {
            // Three of a Kind vs lower categories
            "'7H 7D 7S QD 2C' | 'AH AD KC KD 3S' | BLACK | 'Black wins - three of a kind'",
            "'2H 2D 2S 3C 4H' | '9H 9D AC 7D 4S' | BLACK | 'Black wins - three of a kind'",
            "'3H 3D 3S 2C 4H' | 'AH KD QC JS 9S' | BLACK | 'Black wins - three of a kind'",
            
            // Compare by trips rank
            "'AH AD AS KC 2D' | 'KH KD KS QC 3D' | BLACK | 'Black wins - three of a kind: Ace'",
            "'KH KD KS QC 3D' | 'QH QD QS JC 2D' | BLACK | 'Black wins - three of a kind: King'",
            "'3H 3D 3S 2C 4H' | '2H 2D 2S AC KH' | BLACK | 'Black wins - three of a kind: 3'",
            
            // Same trips, compare by first kicker
            "'7H 7D 7S QD 2C' | '7C 7S 7H JD 3C' | BLACK | 'Black wins - three of a kind: Queen'",
            "'9H 9D 9S AC 2C' | '9C 9S 9H KD 3C' | BLACK | 'Black wins - three of a kind: Ace'",
            
            // Same trips and first kicker, compare by second kicker
            "'7H 7D 7S QD 5C' | '7C 7S 7H QC 4D' | BLACK | 'Black wins - three of a kind: 5'",
            "'5H 5D 5S KC 3C' | '5C 5S 5H KD 2D' | BLACK | 'Black wins - three of a kind: 3'",
            
            // Identical three of a kind - tie
            "'7H 7D 7S QD 2C' | '7C 7S 7H QC 2D' | TIE   | 'Tie'"
        })
        @DisplayName("Three of a kind comparison scenarios")
        void threeOfKindComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
            // When
            ComparisonResult result = useCase.compareHands(blackHand, whiteHand);
            
            // Then
            assertThat(result.getWinner()).isEqualTo(expectedWinner);
            assertThat(result.describe()).contains(expectedDescription);
        }
    }
    
    @Nested
    @DisplayName("Story 6: Straight")
    class StraightScenarios {
        
        @ParameterizedTest
        @CsvSource(delimiter = '|', value = {
            // Straight vs lower categories
            "'9H 8D 7S 6C 5H' | '7H 7D 7S QD 2C' | BLACK | 'Black wins - straight'",
            "'5H 4D 3S 2C AH' | 'AH AD KC KD 3S' | BLACK | 'Black wins - straight'",
            "'6H 5D 4S 3C 2H' | '9H 9D AC 7D 4S' | BLACK | 'Black wins - straight'",
            
            // Compare straights by high card
            "'TH 9D 8S 7C 6H' | '9H 8D 7S 6C 5D' | BLACK | 'Black wins - straight: 10'",
            "'9H 8D 7S 6C 5D' | '8H 7D 6S 5C 4D' | BLACK | 'Black wins - straight: 9'",
            "'AH KD QS JC TH' | 'KH QD JS TC 9D' | BLACK | 'Black wins - straight: Ace'",
            
            // A-low straight (wheel)
            "'5H 4D 3S 2C AH' | '6H 5D 4S 3C 2D' | WHITE | 'White wins - straight: 6'",
            "'6H 5D 4S 3C 2D' | '5H 4D 3S 2C AH' | BLACK | 'Black wins - straight: 6'",
            
            // Identical straights - tie
            "'9H 8D 7S 6C 5H' | '9C 8H 7D 6S 5C' | TIE   | 'Tie'"
        })
        @DisplayName("Straight comparison scenarios")
        void straightComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
            // When
            ComparisonResult result = useCase.compareHands(blackHand, whiteHand);
            
            // Then
            assertThat(result.getWinner()).isEqualTo(expectedWinner);
            assertThat(result.describe()).contains(expectedDescription);
        }
    }
    
    @Nested
    @DisplayName("Story 7: Flush")
    class FlushScenarios {
        
        @ParameterizedTest
        @CsvSource(delimiter = '|', value = {
            // Flush vs lower categories
            "'AH QH 9H 5H 2H' | '9H 8D 7S 6C 5H' | BLACK | 'Black wins - flush'",
            "'2D 4D 6D 8D TD' | '7H 7D 7S QD 2C' | BLACK | 'Black wins - flush'",
            "'3S 5S 7S 9S JS' | 'AH AD KC KD 3S' | BLACK | 'Black wins - flush'",
            
            // Compare flushes by highest card
            "'AH KH 9H 5H 2H' | 'KD QD JD 9D 7D' | BLACK | 'Black wins - flush: Ace'",
            "'KS QS JS 9S 7S' | 'QH JH TH 8H 6H' | BLACK | 'Black wins - flush: King'",
            
            // Same high card, compare by next
            "'AH KH 9H 5H 2H' | 'AC QC JC TC 9C' | BLACK | 'Black wins - flush: King'",
            "'KH QH JH 9H 7H' | 'KD QD JD 9D 6D' | BLACK | 'Black wins - flush: 7'",
            
            // Identical flushes - tie
            "'AH KH 9H 5H 2H' | 'AD KD 9D 5D 2D' | TIE   | 'Tie'"
        })
        @DisplayName("Flush comparison scenarios")
        void flushComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
            // When
            ComparisonResult result = useCase.compareHands(blackHand, whiteHand);
            
            // Then
            assertThat(result.getWinner()).isEqualTo(expectedWinner);
            assertThat(result.describe()).contains(expectedDescription);
        }
    }
    
    @Nested
    @DisplayName("Story 8: Full House")
    class FullHouseScenarios {
        
        @ParameterizedTest
        @CsvSource(delimiter = '|', value = {
            // Full House vs lower categories
            "'3H 3D 3S 9C 9D' | 'AH QH 9H 5H 2H' | BLACK | 'Black wins - full house'",
            "'2H 2D 2S 3C 3H' | '9H 8D 7S 6C 5H' | BLACK | 'Black wins - full house'",
            "'4H 4D 4S 2C 2H' | '7H 7D 7S QD 2C' | BLACK | 'Black wins - full house'",
            
            // Compare by trips rank (trips decide, not pair)
            "'TH TD TS 2C 2D' | '9H 9D 9S AC AD' | BLACK | 'Black wins - full house: 10'",
            "'9H 9D 9S AC AD' | '8H 8D 8S KC KD' | BLACK | 'Black wins - full house: 9'",
            "'AH AD AS KC KD' | 'KH KD KS QC QD' | BLACK | 'Black wins - full house: Ace'",
            
            // Same trips, compare by pair rank
            "'7H 7D 7S QC QD' | '7C 7S 7H JD JC' | BLACK | 'Black wins - full house: Queen'",
            "'5H 5D 5S AC AD' | '5C 5S 5H KD KC' | BLACK | 'Black wins - full house: Ace'",
            
            // Identical full houses - tie
            "'3H 3D 3S 9C 9D' | '3C 3S 3H 9S 9H' | TIE   | 'Tie'"
        })
        @DisplayName("Full house comparison scenarios")
        void fullHouseComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
            // When
            ComparisonResult result = useCase.compareHands(blackHand, whiteHand);
            
            // Then
            assertThat(result.getWinner()).isEqualTo(expectedWinner);
            assertThat(result.describe()).contains(expectedDescription);
        }
    }
    
    @Nested
    @DisplayName("Story 9: Four of a Kind")
    class FourOfKindScenarios {
        
        @ParameterizedTest
        @CsvSource(delimiter = '|', value = {
            // Four of a Kind vs lower categories
            "'QH QD QS QC 2D' | '3H 3D 3S 9C 9D' | BLACK | 'Black wins - four of a kind'",
            "'2H 2D 2S 2C 3H' | 'AH QH 9H 5H 2H' | BLACK | 'Black wins - four of a kind'",
            "'5H 5D 5S 5C AH' | '9H 8D 7S 6C 5H' | BLACK | 'Black wins - four of a kind'",
            
            // Compare by quads rank
            "'AH AD AS AC 2D' | 'KH KD KS KC 3D' | BLACK | 'Black wins - four of a kind: Ace'",
            "'KH KD KS KC 3D' | 'QH QD QS QC AD' | BLACK | 'Black wins - four of a kind: King'",
            "'3H 3D 3S 3C 2D' | '2H 2D 2S 2C AH' | BLACK | 'Black wins - four of a kind: 3'",
            
            // Same quads, compare by kicker
            "'7H 7D 7S 7C QD' | '7C 7S 7H 7D JC' | BLACK | 'Black wins - four of a kind: Queen'",
            "'9H 9D 9S 9C AC' | '9C 9S 9H 9D KD' | BLACK | 'Black wins - four of a kind: Ace'",
            
            // Identical four of a kind - tie
            "'QH QD QS QC 2D' | 'QC QS QH QD 2H' | TIE   | 'Tie'"
        })
        @DisplayName("Four of a kind comparison scenarios")
        void fourOfKindComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
            // When
            ComparisonResult result = useCase.compareHands(blackHand, whiteHand);
            
            // Then
            assertThat(result.getWinner()).isEqualTo(expectedWinner);
            assertThat(result.describe()).contains(expectedDescription);
        }
    }
    
    @Nested
    @DisplayName("Story 10: Straight Flush")
    class StraightFlushScenarios {
        
        @ParameterizedTest
        @CsvSource(delimiter = '|', value = {
            // Straight Flush vs lower categories
            "'9H TH JH QH KH' | 'QH QD QS QC 2D' | BLACK | 'Black wins - straight flush'",
            "'AH 2H 3H 4H 5H' | '3H 3D 3S 9C 9D' | BLACK | 'Black wins - straight flush'",
            "'6D 7D 8D 9D TD' | 'AH QH 9H 5H 2H' | BLACK | 'Black wins - straight flush'",
            
            // Compare straight flushes by high card
            "'TH JH QH KH AH' | '9D TD JD QD KD' | BLACK | 'Black wins - straight flush: Ace'",
            "'9S TS JS QS KS' | '8H 9H TH JH QH' | BLACK | 'Black wins - straight flush: King'",
            "'6C 7C 8C 9C TC' | '5D 6D 7D 8D 9D' | BLACK | 'Black wins - straight flush: 10'",
            
            // A-low straight flush (steel wheel)
            "'AH 2H 3H 4H 5H' | '2D 3D 4D 5D 6D' | WHITE | 'White wins - straight flush: 6'",
            "'2S 3S 4S 5S 6S' | 'AS 2S 3S 4S 5S' | BLACK | 'Black wins - straight flush: 6'",
            
            // Royal flush vs lower straight flush
            "'TH JH QH KH AH' | '9H TH JH QH KH' | BLACK | 'Black wins - straight flush: Ace'",
            
            // Identical straight flushes - tie
            "'9H TH JH QH KH' | '9D TD JD QD KD' | TIE   | 'Tie'"
        })
        @DisplayName("Straight flush comparison scenarios")
        void straightFlushComparisonScenarios(String blackHand, String whiteHand, Winner expectedWinner, String expectedDescription) {
            // When
            ComparisonResult result = useCase.compareHands(blackHand, whiteHand);
            
            // Then
            assertThat(result.getWinner()).isEqualTo(expectedWinner);
            assertThat(result.describe()).contains(expectedDescription);
        }
    }
}
