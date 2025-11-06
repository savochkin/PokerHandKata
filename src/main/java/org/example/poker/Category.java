package org.example.poker;

public enum Category {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR;
    
    public int getStrength() {
        return ordinal();
    }
}
