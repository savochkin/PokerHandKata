package org.example.poker;

import java.util.List;

public record HandRank(Category category, List<Rank> kickers) {
}
