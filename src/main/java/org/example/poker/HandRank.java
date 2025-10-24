package org.example.poker;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record HandRank(Category category, List<Rank> kickers) {
}
