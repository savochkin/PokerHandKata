package org.example.poker;

import java.util.List;

public class HandRank {
    private final Category category;
    private final List<Rank> kickers;
    
    // TODO: Task 4 - This class has too much boilerplate code. Can you improve it?
    // Hint: Look at other classes in the project - they use something to reduce boilerplate
    // Remove the manual constructor and getters, add @Getter and @RequiredArgsConstructor
    
    public HandRank(Category category, List<Rank> kickers) {
        this.category = category;
        this.kickers = kickers;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public List<Rank> getKickers() {
        return kickers;
    }
}
