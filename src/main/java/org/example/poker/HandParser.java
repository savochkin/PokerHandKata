package org.example.poker;

import java.util.ArrayList;
import java.util.List;

public class HandParser {
    
    public static Hand parseHand(String handString) {
        if (handString == null || handString.trim().isEmpty()) {
            throw new IllegalArgumentException("Hand string cannot be empty");
        }
        
        String[] tokens = handString.trim().split("\\s+");
        
        if (tokens.length != 5) {
            throw new IllegalArgumentException("A hand must contain exactly 5 cards, got " + tokens.length);
        }
        
        List<Card> cards = new ArrayList<>();
        for (String token : tokens) {
            try {
                cards.add(Card.parse(token));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid card token: " + token + " - " + e.getMessage(), e);
            }
        }
        
        return new Hand(cards);
    }
}
