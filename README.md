# Poker Hand Kata - Story 2 Refactoring Tasks

A TDD and refactoring kata for improving existing code through test-driven bug fixes and systematic refactoring.

## 🎯 What You Have

You have a **working implementation** of Story 2 (Compare High Card Hands) from the Poker Hand Kata. The code can:
- Parse poker hands: `Hand.parse("AH KD 9C 7D 4S")`
- Rank hands: `hand.rank()` returns category and kickers
- Compare hands: `black.compare(white)` returns winner and description

However, there are **intentional issues** that need to be fixed through TDD and refactoring practices.

## 🎓 Learning Objectives

By completing these tasks, you will practice:
- **TDD** - Writing tests first, then fixing code
- **Refactoring** - Improving code while keeping tests green
- **Lombok** - Reducing boilerplate code
- **Builder Pattern** - Improving code readability
- **Immutability** - Protecting objects from unwanted modifications

## 📋 The Tasks

### Task 1: Fix the Bug - White Wins Reported as Black Wins (TDD)

**Problem:** You received a bug report: *"When White wins, the system incorrectly reports that Black wins!"*

**Your Mission:**

**Step 0: Check Test Coverage (Optional but Recommended)**
1. Run tests with coverage in your IDE (IntelliJ: Run → Run with Coverage)
2. Open `Hand.java` and look at the `compare()` method
3. Notice the `else if (comparison < 0)` branch - is it covered?
4. **Observation:** The bug exists in an untested code path!

**Step 1: 🔴 RED** - Write a failing test that demonstrates the bug
1. Open `CompareHighCardHandsTest.java`
2. Add a test for: Black `2H 3D 5S 9C KD` vs White `2C 3H 4S 8C AH`
3. White should win (Ace beats King)
4. Run the test - it should fail (shows the bug)
5. Run with coverage again - now the buggy branch is covered!

**Step 2: 🟢 GREEN** - Fix the bug
1. Open `Hand.java`, find the `compare()` method
2. Look at line ~104: `return new ComparisonResult(Winner.BLACK, otherKickers.get(i));`
3. Fix: Change `Winner.BLACK` to `Winner.WHITE`
4. Run the test - it should pass

**Step 3: 🔵 REFACTOR** - Clean up if needed

**Key Lesson:** Code coverage helps identify untested paths where bugs can hide!

**Expected Test:**
```java
@Test
void shouldReportWhiteWinsWhenWhiteHasHigherCard() {
    // Given
    Hand black = Hand.parse("2H 3D 5S 9C KD");
    Hand white = Hand.parse("2C 3H 4S 8C AH");
    
    // When
    ComparisonResult result = black.compare(white);
    
    // Then
    assertThat(result.getWinner()).isEqualTo(Winner.WHITE);
    assertThat(result.describe()).isEqualTo("White wins - high card: Ace");
}
```

---

### Task 2: Refactor Tests - Reduce Duplication with Parameterized Tests

**Problem:** The test class has many similar tests that follow the same pattern, making it harder to maintain.

**Your Mission:**
1. **🟢 GREEN** - Ensure all tests pass before refactoring
2. **🔵 REFACTOR** - Consolidate similar tests into parameterized tests
   - Use `@ParameterizedTest` and `@CsvSource`
   - Example:
   ```java
   @ParameterizedTest
   @CsvSource({
       "'2H 3D 5S 9C KD', '2C 3H 4S 8C AH', WHITE, 'White wins - high card: Ace'",
       "'2H 3D 5S 9C KD', '2C 3H 4S 8C KH', BLACK, 'Black wins - high card: 9'"
   })
   void shouldCompareHighCardHands(String blackHand, String whiteHand, 
                                   Winner expectedWinner, String expectedDescription) {
       // Test implementation
   }
   ```
3. **🟢 GREEN** - Run tests to ensure they still pass

---

### Task 3: Remove Boilerplate - Use Lombok in HandRank

**Problem:** The `HandRank` class contains boilerplate code (constructor, getters). Look at the TODO comment.

**Your Mission:**
1. **🟢 GREEN** - Ensure all tests pass before refactoring
2. **🔵 REFACTOR** - Apply Lombok annotations
   - Open `HandRank.java`
   - Remove manual constructor and getters
   - Add `@Getter` and `@RequiredArgsConstructor`
3. **🟢 GREEN** - Run tests to ensure they still pass

**Before (Boilerplate):**
```java
public class HandRank {
    private final Category category;
    private final List<Rank> kickers;
    
    public HandRank(Category category, List<Rank> kickers) { ... }
    public Category getCategory() { ... }
    public List<Rank> getKickers() { ... }
}
```

**After (Lombok):**
```java
@Getter
@RequiredArgsConstructor
public class HandRank {
    private final Category category;
    private final List<Rank> kickers;
}
```

---

### Task 4: Fix Bug & Improve Readability with Builder Pattern (TDD + Refactoring)

**Problem:** Bug report: *"The losingRank in ComparisonResult is sometimes incorrect!"*

Investigation shows the bug is in `Hand.compare()`:
```java
return new ComparisonResult(Winner.BLACK, thisKickers.get(i), thisKickers.get(i));
//                                         ^^^^^^^^^^^^^^^^^^  ^^^^^^^^^^^^^^^^^^
//                                         Same variable used twice!
```

**Root cause:** Three parameters, two of the same type (`Rank`). Easy to copy-paste the wrong variable. The compiler can't catch this because both are `Rank`.

**Your Mission:**

**Step 1: 🔴 RED** - Write a test to expose the bug
1. Open `CompareHighCardHandsTest.java`
2. Add a test that checks `getLosingRank()`:
   ```java
   @Test
   void shouldSetCorrectLosingRankWhenBlackWins() {
       Hand black = Hand.parse("AH KD 9C 7D 4S");
       Hand white = Hand.parse("AH KD 9C 7D 3S");
       
       ComparisonResult result = black.compare(white);
       
       assertThat(result.getWinningRank()).isEqualTo(Rank.FOUR);
       assertThat(result.getLosingRank()).isEqualTo(Rank.THREE); // This will fail!
   }
   ```
3. Run the test - it should fail (bug exposed)

**Step 2: 🟢 GREEN** - Fix the bug (but notice the code is still hard to read)
1. Open `Hand.java`, line ~100
2. Change `thisKickers.get(i)` to `otherKickers.get(i)` for the third parameter
3. Run tests - they should pass

**Step 3: 🔵 REFACTOR** - Prevent future bugs with builder pattern
1. Open `ComparisonResult.java`, add `@Builder` and `@AllArgsConstructor`
2. Open `Hand.java`, replace all constructor calls with builder:
   ```java
   return ComparisonResult.builder()
       .winner(Winner.BLACK)
       .winningRank(thisKickers.get(i))
       .losingRank(otherKickers.get(i))  // Now it's obvious which is which!
       .build();
   ```
3. Run tests - they should still pass

**Key Lessons:**
- Positional parameters with same types are error-prone
- Builder pattern makes code self-documenting
- Named parameters prevent copy-paste mistakes

---

### Task 5: Fix Immutability Bug - Protect Hand Cards (TDD)

**Problem:** Bug report: *"We're seeing incorrect comparison results! Sometimes a hand that should win is losing."*

**Your investigation:**
After debugging, you discovered the root cause:
```java
// Client code somewhere in the system
Hand hand = Hand.parse("AH KD 9C 7D 4S");
hand.getCards().clear(); // Oops! Hand is now empty
// Later comparisons fail because the hand has no cards!
```

**Root cause:** `Hand.getCards()` returns a mutable list. Client code can accidentally (or intentionally) modify the hand's internal state, breaking the comparison logic.

**Your Mission:**

**Step 1: 🔴 RED** - Write tests that demonstrate the vulnerability
```java
@Test
void shouldReturnUnmodifiableCardsList() {
    Hand hand = Hand.parse("AH KD 9C 7D 4S");
    
    assertThatThrownBy(() -> hand.getCards().clear())
        .isInstanceOf(UnsupportedOperationException.class);
}

@Test
void shouldNotAllowAddingCards() {
    Hand hand = Hand.parse("AH KD 9C 7D 4S");
    Card newCard = new Card(Rank.TWO, Suit.HEARTS);
    
    assertThatThrownBy(() -> hand.getCards().add(newCard))
        .isInstanceOf(UnsupportedOperationException.class);
}
```
Run tests - they will fail (vulnerability exposed)

**Step 2: 🟢 GREEN** - Fix the implementation
1. Open `Hand.java`
2. Override Lombok's generated `getCards()` method
3. Return `Collections.unmodifiableList(cards)` instead
4. Add import: `import java.util.Collections;`

```java
// Override Lombok's generated getter
public List<Card> getCards() {
    return Collections.unmodifiableList(cards);
}
```

**Step 3: 🔵 REFACTOR** - Verify the solution is clean

**Key Lesson:** Always return defensive copies or unmodifiable collections from getters to protect internal state!

---

### Task 6: Remove Implementation-Tied Tests (Behavioral Testing)

**Problem:** The `rank()` method is currently public and has its own test class (`HandRankTest`) that checks internal implementation details (`HandRank` structure) rather than observable behavior (comparison results).

**Why this matters:** 
- The `rank()` method is an implementation detail used internally by `compare()`
- Testing implementation details creates brittle tests that break when you refactor internal code, even if behavior stays correct
- Behavioral tests are more resilient and provide better confidence

**Your Mission:**
1. Open `Hand.java` and change `rank()` from `public` to `private`
2. Delete the entire `HandRankTest.java` file
3. Run remaining tests - they should all still pass
4. (Optional) Run tests with coverage - observe that `rank()` is still 100% covered through the `compare()` method calls in `CompareHighCardHandsTest`
5. Key takeaway: The behavioral tests give us confidence without testing implementation details, and we haven't lost any coverage!

**Key Insight:** 
- ❌ **Implementation test:** Calls `hand.rank()` and checks `rank.getKickers().containsExactly(ACE, KING...)`
- ✅ **Behavioral test:** Calls `black.compare(white)` and checks `result.describe().equals("White wins - high card: Ace")`

The behavioral tests verify the system works correctly without coupling to internal structure. By making `rank()` private, we enforce that clients use the public API (`compare()`) instead of implementation details.

## 🛠️ Technical Requirements

### Required Technologies
- **Java 21**
- **Maven** - Build tool
- **JUnit 5** - Testing framework (including `@ParameterizedTest`)
- **AssertJ** - Fluent assertions
- **Lombok** - Reduce boilerplate code

### Key Patterns You'll Use

**Lombok Annotations:**
```java
@Getter                      // Generate getters
@RequiredArgsConstructor     // Constructor with final fields
@Builder                     // Builder pattern
@AllArgsConstructor          // Constructor with all fields
```

**Parameterized Tests:**
```java
@ParameterizedTest
@CsvSource({
    "'hand1', 'hand2', EXPECTED_WINNER, 'expected description'",
    "'hand3', 'hand4', EXPECTED_WINNER, 'expected description'"
})
void testName(String hand1, String hand2, Winner winner, String description) {
    // Test implementation
}
```

**AssertJ Assertions:**
```java
assertThat(result.getWinner()).isEqualTo(Winner.WHITE);
assertThat(result.describe()).isEqualTo("White wins - high card: Ace");
assertThatThrownBy(() -> list.clear())
    .isInstanceOf(UnsupportedOperationException.class);
```

**Immutability:**
```java
// Return unmodifiable collections
public List<Rank> getKickers() {
    return Collections.unmodifiableList(kickers);
}
```

## 🚀 Getting Started

### Prerequisites
Make sure Java 21 is installed and JAVA_HOME is set:

```bash
# Check Java version
java -version

# Set JAVA_HOME (add to your ~/.zshrc or ~/.bash_profile)
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

### Run Tests

```bash
# Run all tests
./mvnw.sh test

# Run Story 2 tests only
./mvnw.sh test -Dtest=CompareHighCardHandsTest

# Clean and test
./mvnw.sh clean test
```

**Expected Initial State:**
- ✅ 6 tests passing in CompareHighCardHandsTest (4 comparison tests) + HandRankTest (2 ranking tests)
- ❌ Missing test for "White wins" scenario (Task 1)
- ⚠️ `rank()` method is public (should be private - Task 6)

### How to Complete the Tasks

Follow the tasks in order (1 → 2 → 3 → 4 → 5 → 6). Each task has:
- **TODO comments** in the code to guide you
- **Clear instructions** in the task description above
- **Expected outcomes** to verify your work

**Look for TODO comments:**
```bash
# Find all TODOs in the code
grep -r "TODO:" src/
```

**Files with TODOs:**
- `CompareHighCardHandsTest.java` - Tasks 1, 2, 5
- `HandRank.java` - Task 3
- `Hand.java` - Task 4
- `ComparisonResult.java` - Task 4
- `Hand.java` - Task 6 (make rank() private)
- `HandRankTest.java` - Task 6 (delete this file)

## 📁 Project Structure

```
src/
├── main/java/org/example/poker/
│   ├── Card.java               # Single card (rank + suit)
│   ├── Rank.java               # Card ranks enum (2-A)
│   ├── Suit.java               # Card suits enum (C,D,H,S)
│   ├── Hand.java               # 5-card hand with compare logic - Task 4, 6
│   ├── Category.java           # Hand categories (HIGH_CARD)
│   ├── HandRank.java           # Ranking result (category + kickers) - Task 3
│   ├── Winner.java             # Winner enum (BLACK, WHITE, TIE)
│   └── ComparisonResult.java   # Comparison result with description - Task 4
└── test/java/org/example/poker/
    ├── HandRankTest.java              # Implementation tests - DELETE in Task 6
    └── CompareHighCardHandsTest.java  # Behavioral tests - Tasks 1, 2, 5
```

## 📊 Progress Tracking

- [ ] **Task 1:** Write test for White wins + fix bug (TDD)
- [ ] **Task 2:** Refactor tests to parameterized tests
- [ ] **Task 3:** Remove boilerplate from HandRank using Lombok
- [ ] **Task 4:** Write test for losingRank bug + fix + refactor with builder (TDD + Refactoring)
- [ ] **Task 5:** Write test for immutability + fix Hand.getCards() (TDD)
- [ ] **Task 6:** Remove implementation-tied tests (behavioral testing)

## 🔄 TDD & Refactoring Workflow

### Tasks 1, 4, 5: Test-Driven Development (TDD)
1. **🔴 RED** - Write a failing test first
2. **🟢 GREEN** - Write minimal code to make it pass
3. **🔵 REFACTOR** - Clean up the code

**Key principle:** Let tests drive your implementation!

**Task 4 combines TDD + Refactoring:** Write test → Fix bug → Refactor with builder

### Tasks 2, 3, 6: Refactoring Discipline
1. **🟢 GREEN** - Ensure all tests pass before refactoring
2. **🔵 REFACTOR** - Change implementation/tests without changing behavior
3. **🟢 GREEN** - Verify all tests still pass

**Key principle:** Tests protect you during refactoring!

## 💡 Tips

- **Task 1:** Look at existing comparison tests for the pattern; check coverage first
- **Task 2:** Use `@ParameterizedTest` with `@CsvSource` - check JUnit 5 docs
- **Task 3:** Look at other classes (Card, Rank, Suit) for Lombok examples
- **Task 4:** Write test for `getLosingRank()` first; builder makes parameters explicit
- **Task 5:** Use `Collections.unmodifiableList()` to wrap the list; override Lombok's getter
- **Task 6:** Notice which tests check internal structure vs observable behavior; run with coverage to verify `rank()` is still covered
- **Run tests frequently** - Get immediate feedback after each change
- **Follow TODO comments** - They guide you step by step

## 🆘 Common Issues

**Tests not running?**
```bash
# Make sure JAVA_HOME is set
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# Try cleaning first
./mvnw.sh clean test
```

**Lombok not working?**
- Rebuild the project after adding annotations
- Check that Lombok is in pom.xml
- Ensure your IDE has Lombok plugin installed

**Parameterized tests not working?**
- Import: `org.junit.jupiter.params.ParameterizedTest`
- Import: `org.junit.jupiter.params.provider.CsvSource`
- Check the syntax of your `@CsvSource` carefully

**Can't import Collections?**
- Add: `import java.util.Collections;`

## 🎯 Success Criteria

You've completed the kata when:
- ✅ All 6 tasks completed in order
- ✅ All tests passing (including new tests from Tasks 1, 4 & 5)
- ✅ Tests use parameterized tests where appropriate (Task 2)
- ✅ HandRank uses Lombok annotations (Task 3)
- ✅ ComparisonResult uses builder pattern (Task 4)
- ✅ Hand.getCards() returns unmodifiable list (Task 5)
- ✅ Implementation-tied tests removed and `rank()` is private (Task 6)
- ✅ No compilation errors or warnings
- ✅ Code is clean and readable

## 💬 Discussion Points

After completing all tasks, reflect on:
- **Task 1:** How did TDD help you catch bugs early?
- **Task 2:** What are the benefits of parameterized tests?
- **Task 3:** How much boilerplate did Lombok eliminate?
- **Task 4:** When should you use builder pattern vs constructors?
- **Task 5:** Why is immutability important in domain objects?
- **Task 6:** Why are behavioral tests more resilient than implementation-tied tests?

---

**Ready to start?** Run `./mvnw.sh test -Dtest=CompareHighCardHandsTest` and begin with Task 1! 🚀
