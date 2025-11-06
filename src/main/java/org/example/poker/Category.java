package org.example.poker;

public enum Category {
    HIGH_CARD,
    ONE_PAIR;
    
    public int getStrength() {
        return ordinal();
    }
}
