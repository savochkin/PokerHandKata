package org.example.poker;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HandRank {
    private final Category category;
    private final List<Rank> kickers;
}
