# Poker Hand Kata

## Problem Description

A poker deck contains 52 cards - each card has a suit which is one of clubs, diamonds, hearts, or spades (denoted C, D, H, and S in the input data).

Each card also has a value which is one of 2, 3, 4, 5, 6, 7, 8, 9, 10, jack, queen, king, ace (denoted 2, 3, 4, 5, 6, 7, 8, 9, T, J, Q, K, A).

For scoring purposes, the suits are unordered while the values are ordered as given above, with 2 being the lowest and ace the highest value.

A poker hand consists of 5 cards dealt from the deck. Poker hands are ranked by the following partial order from lowest to highest.

### Hand Rankings

**High Card:**
Hands which do not fit any higher category are ranked by the value of their highest card. If the highest cards have the same value, the hands are ranked by the next highest, and so on.

**Pair:**
2 of the 5 cards in the hand have the same value. Hands which both contain a pair are ranked by the value of the cards forming the pair. If these values are the same, the hands are ranked by the values of the cards not forming the pair, in decreasing order.

**Two Pairs:**
The hand contains 2 different pairs. Hands which both contain 2 pairs are ranked by the value of their highest pair. Hands with the same highest pair are ranked by the value of their other pair. If these values are the same the hands are ranked by the value of the remaining card.

**Three of a Kind:**
Three of the cards in the hand have the same value. Hands which both contain three of a kind are ranked by the value of the 3 cards.

**Straight:**
Hand contains 5 cards with consecutive values. Hands which both contain a straight are ranked by their highest card.

**Flush:**
Hand contains 5 cards of the same suit. Hands which are both flushes are ranked using the rules for High Card.

**Full House:**
3 cards of the same value, with the remaining 2 cards forming a pair. Ranked by the value of the 3 cards.

**Four of a kind:**
4 cards with the same value. Ranked by the value of the 4 cards.

**Straight flush:**
5 cards of the same suit with consecutive values. Ranked by the highest card in the hand.

### Task

Your job is to rank pairs of poker hands and to indicate which, if either, has a higher rank.

### Examples

```
Input: Black: 2H 3D 5S 9C KD White: 2C 3H 4S 8C AH
Output: White wins - high card: Ace

Input: Black: 2H 4S 4C 2D 4H White: 2S 8S AS QS 3S
Output: Black wins - full house

Input: Black: 2H 3D 5S 9C KD White: 2C 3H 4S 8C KH
Output: Black wins - high card: 9

Input: Black: 2H 3D 5S 9C KD White: 2D 3H 5C 9S KH
Output: Tie
```

## Design Description

**Goal:** A tiny service/library that scores and compares poker hands (5-card, standard rules) and exposes two primitives:

1. `rank(hand) -> HandRank` — classify a single 5-card hand and its tie-breakers
2. `compare(handA, handB) -> -1 | 0 | 1` — decide who wins

### Core Domain Model

**Card** `{ rank: 2..A, suit: ♣♦♥♠ }`

**Hand** = `Card[5]` (no duplicates)

**Category** (ordered strongest→weakest):
- StraightFlush > FourKind > FullHouse > Flush > Straight > ThreeKind > TwoPair > OnePair > HighCard

**HandRank** `{ category, primaryKickers: number[], secondaryKickers: number[] }`
- A normalized, comparable tuple; e.g., ThreeKind uses [rankOfTrips] then kickers [k1,k2]

### Comparison Rules (Tie-breakers by Category)

- **Straight Flush / Straight:** Compare highest card; note A-low (A-2-3-4-5) highest is 5.
- **Four of a Kind:** Compare quad rank, then kicker.
- **Full House:** Compare trips rank, then pair rank.
- **Flush:** Compare sorted ranks high→low lexicographically.
- **Three of a Kind:** Compare trips rank, then remaining kickers high→low.
- **Two Pair:** Compare higher pair, then lower pair, then kicker.
- **One Pair:** Compare pair rank, then three kickers high→low.
- **High Card:** Compare five cards high→low.

### Assumptions / Constraints

- Input is 5 distinct cards
- Ranks: 2 3 4 5 6 7 8 9 T J Q K A
- Suits are equal in strength (no suit ordering)
- **Non-goals:** 7-card evaluation, jokers/wilds, betting logic, multi-hand tables, performance optimizations

## Story Breakdown for Incremental TDD

### Breakdown Principles

**1. Customer Value First:** Each story delivers a working feature that users can immediately benefit from, not just internal detection logic.

**2. Incremental Implementation Strategy:** We start with High Card (the simplest/weakest category) and progressively add stronger hand types. This works because:
   - The final `rank()` implementation will check categories from **strongest to weakest**
   - High Card becomes the **fallback case** (if nothing else matches, it's High Card)
   - Each new story adds one more check before falling through to simpler cases
   - No need to validate against future categories - just assume valid input for current story

**3. Test-Driven:** Each story has concrete acceptance criteria you can implement with tests (GWT or xUnit). Rotate every 5–7 minutes, strict red→green→refactor.

### Story 1 — Parse & Validate Cards

**Value:** We can reliably accept hands from outside callers; everything else depends on this.

**Build:** `parseHand("AH KD 3C TD 9S") -> Hand` with validation.

**Acceptance Criteria:**
- Given a well-formed string "AH KD 3C TD 9S", when parsed, then we get 5 cards with correct ranks/suits.
- Given duplicate cards "AH AH 3C TD 9S", parsing fails with a validation error.
- Given malformed tokens "AX KD ?? TD 9S", parsing fails with a clear error message.

### Story 2 — Compare High Card Hands

**Value:** Users can compare any two 5-card hands without pairs, straights, or flushes - the simplest but most common scenario.

**Build:** 
- `rank()` returns HighCard with a normalized list of kickers
- `compare(handA, handB)` determines winner by comparing kickers lexicographically

**Acceptance Criteria:**
- "AH KD 9C 7D 4S" → HighCard with kickers [A,K,9,7,4].
- "2H 3D 5C 9S JD" → HighCard with [J,9,5,3,2].
- "AH KD 9C 7D 4S" vs "KH QD 9C 7D 4S" → First hand wins (A > K).
- "AH KD 9C 7D 4S" vs "AS KC 9D 7C 4H" → Tie (all kickers equal).
- "AH KD 9C 7D 4S" vs "AH KD 9C 7D 3S" → First hand wins (4 > 3 in last position).

### Story 3 — Detect One Pair (+ Tiebreakers)

**Value:** Covers the most common made hand; many real comparisons now work.

**Build:** `rank()` identifies OnePair and supplies [pair, k1, k2, k3].

**Acceptance Criteria:**
- "AH AD 9C 7D 4S" → OnePair(A) + kickers [9,7,4].
- Pair vs pair: "AH AD 9C 7D 4S" beats "KH KD QS JD 2C".
- Same pair, compare kickers: "9H 9D AK 7C 3S" beats "9C 9S AQ 7D 2H".

### Story 4 — Two Pair

**Value:** Supports a very frequent showdown outcome for users.

**Build:** `rank()` identifies TwoPair and uses [(highPair),(lowPair),(kicker)].

**Acceptance Criteria:**
- "AH AD KC KD 3S" → TwoPair(A,K) kicker 3.
- Compare higher pair first, then lower pair, then kicker.

### Story 5 — Three of a Kind

**Value:** Expands meaningful comparisons; common in 5-card draws.

**Build:** `rank()` identifies trips and kickers.

**Acceptance Criteria:**
- "7H 7D 7S QD 2C" → ThreeKind(7) + kickers [Q,2].
- Trips outrank any TwoPair & OnePair.

### Story 6 — Straight (incl. A-low)

**Value:** First sequence-based category; correctness around Aces is business-critical.

**Build:** Detect five consecutive ranks; treat A 2 3 4 5 as straight to 5.

**Acceptance Criteria:**
- "5H 4D 3S 2C AH" → Straight(5) (A-low).
- "9H 8D 7S 6C 5H" → Straight(9).
- A-high straight "T J Q K A" beats "9 T J Q K".

### Story 7 — Flush

**Value:** Suit-based category; lots of real hands now covered.

**Build:** Five cards same suit; compare lexicographically by ranks.

**Acceptance Criteria:**
- "AH QH 9H 5H 2H" → Flush with kickers [A,Q,9,5,2].
- Flush beats any Straight; among flushes, highest card wins, then next…

### Story 8 — Full House

**Value:** Important premium category; many tie-break edges.

**Build:** Detect 3 + 2; compare trips rank, then pair rank.

**Acceptance Criteria:**
- "3H 3D 3S 9C 9D" → FullHouse(3 over 9).
- FullHouse(10 over 2) beats FullHouse(9 over A) because trips decide.

### Story 9 — Four of a Kind

**Value:** Near-top category; end-game correctness matters for payouts.

**Build:** Detect quads; compare quad rank then kicker.

**Acceptance Criteria:**
- "QH QD QS QC 2D" → FourKind(Q) kicker 2.
- Quads beat Full House; among quads, higher quad wins; tie → higher kicker.

### Story 10 — Straight Flush (incl. Royal)

**Value:** Completes the category ladder.

**Build:** Straight & same suit; A-low applies; "Royal" is just StraightFlush to Ace.

**Acceptance Criteria:**
- "9H TH JH QH KH" → StraightFlush(K) (A-high straight flush if Ace present).
- "AH 2H 3H 4H 5H" → StraightFlush(5) (A-low).
- Any Straight Flush beats Four of a Kind.

### Story 11 — General Comparison Across All Categories

**Value:** Production-ready compare endpoint for any two 5-card hands.

**Build:** `compare()` first by category, then by the normalized tiebreak tuple produced by `rank()`.

**Acceptance Criteria:**
- Provide a table-driven test with at least one comparison per category boundary (e.g., best Flush loses to worst FullHouse, etc.).
- Deterministic ties when two hands are isomorphic (e.g., same ranks/suits permuted).

### Story 12 — Public API / CLI Façade

**Value:** Non-dev users can consume it; great for demos.

**Build:** Thin adapter:
- CLI: `poker compare "AH KD 9C 7D 4S" "KH QD 9C 7D 4S"` → A
- HTTP (optional): `POST /compare {a: "...", b: "..."}`

**Acceptance Criteria:**
- Valid inputs return expected winner or Tie.
- Invalid input returns 400/exit-code≠0 with a helpful error.

### Story 13 — Error Handling & Observability (Tiny)

**Value:** Safer to integrate; easier to debug.

**Build:** Structured errors; minimal metrics hooks (count comparisons, invalid inputs).

**Acceptance Criteria:**
- Invalid hand increments an error counter (or log record).
- Happy-path comparison increments a success counter (or log record).
