package org.example.poker.app.domain;

import java.util.List;

public record HandRank(Category category, List<Rank> kickers) {
}
