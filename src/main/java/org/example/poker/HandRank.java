package org.example.poker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class HandRank {
    private final Category category;
    private final List<Rank> kickers;
}
