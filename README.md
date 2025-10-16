# Poker Hand Kata

A TDD coding kata for learning poker hand evaluation through incremental story implementation.

## 🎯 Kata Description

Build a poker hand evaluator that can parse, validate, and compare poker hands. The kata is divided into stories with clear acceptance criteria. You'll practice Test-Driven Development by implementing features to make failing tests pass and writing new tests for additional functionality.

## 📖 Story 1: Parse & Validate Cards

### Goal
Parse poker hand strings and validate that they represent valid 5-card hands.

### Acceptance Criteria

**AC1: Parse well-formed hand strings**
- Given a string like `"AH KD 3C TD 9S"`, when parsed, then return a Hand with 5 cards
- Each card has a rank (2-9, T, J, Q, K, A) and suit (C, D, H, S)
- Examples:
  - `"2H 3D 5S 9C KD"` → valid hand
  - `"AH KD 3C TD 9S"` → valid hand
  - `"AS KS QS JS TS"` → valid hand

**AC2: Reject wrong number of cards**
- Given a string with fewer or more than 5 cards, when parsing, then throw `IllegalArgumentException`
- Error message should contain "5 cards"
- Examples:
  - `"AH KD 3C TD"` → fails (only 4 cards)
  - `"AH"` → fails (only 1 card)
  - `"AH KD 3C TD 9S 2H"` → fails (6 cards)

**AC3: Reject empty input**
- Given an empty string, when parsing, then throw `IllegalArgumentException`
- Error message should contain "empty"

**AC4: Reject duplicate cards** ⚠️ **Task 1 - TO IMPLEMENT**
- Given a string with duplicate cards, when parsing, then throw `IllegalArgumentException`
- Error message should contain "duplicate"
- Examples:
  - `"AH AH 3C TD 9S"` → fails (duplicate Ace of Hearts)
  - `"2H 2H 2H 3C 4D"` → fails (three duplicate 2 of Hearts)

**AC5: Reject invalid card symbols** ⚠️ **Task 2 - TO IMPLEMENT (TDD)**
- Given invalid rank symbol (e.g., '1', 'X', 'B'), when parsing, then throw `IllegalArgumentException`
- Given invalid suit symbol (e.g., 'X', 'Z'), when parsing, then throw `IllegalArgumentException`
- Error messages should contain "Invalid rank" or "Invalid suit"
- Examples:
  - `"1H KD 3C TD 9S"` → fails (1 is not a valid rank)
  - `"AX KD 3C TD 9S"` → fails (X is not a valid suit)

## 🚀 Getting Started

### Run Tests
```bash
./gradlew test
```

**Expected Result:**
- ✅ 6 tests passing (well-formed input, wrong number of cards, empty input)
- ❌ 3 tests failing (duplicate cards - Task 1)
- 📝 Task 2 tests need to be written (TDD practice)

### Task 1: Implement Duplicate Validation
1. Open `src/test/java/org/example/poker/RejectWrongInputsTest.java`
2. Find the failing test: `givenDuplicateCards_whenParsing_thenFailsWithValidationError`
3. Open `src/main/java/org/example/poker/Hand.java`
4. Find the `validate()` method with TODO
5. Implement duplicate card detection
6. Run tests until all pass

**Hint:** Use a `Set` to detect duplicates

### Task 2: Write Tests for Invalid Symbols (TDD)
1. Open `src/test/java/org/example/poker/RejectWrongInputsTest.java`
2. Find the TODO comments for Task 2
3. **Write tests first** for:
   - Invalid rank symbols
   - Invalid suit symbols
   - Hands containing invalid cards
4. Run tests - they should fail
5. Implement validation in `Rank.fromSymbol()` and `Suit.fromSymbol()`
6. Run tests - they should pass

### Task 3: Refactor to Lombok (Refactoring Practice)
1. Find all classes with `TODO: Task 3` comments:
   - `Card.java` - Has manual getters, constructor, equals/hashCode
   - `Rank.java` - Has manual constructor and getters
   - `Suit.java` - Has manual constructor and getter
2. **Refactor each class** to use Lombok annotations:
   - Replace manual getters with `@Getter`
   - Replace manual constructors with `@AllArgsConstructor` (enums) or `@Builder` (Card)
   - Replace manual equals/hashCode with `@EqualsAndHashCode` (Card only)
3. **Remove the boilerplate code** (getters, constructors, equals, hashCode)
4. Run tests - they should still pass (refactoring shouldn't break tests!)
5. Update `Card.parse()` to use builder pattern: `Card.builder().rank(...).suit(...).build()`

**Important:** This is a refactoring task - the behavior must not change, only the implementation!

## 🛠️ Technical Requirements

### Required Technologies
- **Java 21** (via Gradle toolchain)
- **JUnit 5** - Testing framework
- **AssertJ** - Fluent assertions
- **Lombok** - Reduce boilerplate code

### Code Style Requirements

**1. Use Lombok Annotations**
```java
@Getter                    // Generate getters
@Builder                   // Builder pattern for object construction
@AllArgsConstructor        // Constructor with all fields (for enums)
@EqualsAndHashCode         // Generate equals() and hashCode()
```

**2. Use Builder Pattern**
```java
// Create cards using builder
Card card = Card.builder()
    .rank(Rank.ACE)
    .suit(Suit.HEARTS)
    .build();

// Create hands using builder with @Singular
Hand hand = Hand.builder()
    .card(card1)
    .card(card2)
    .card(card3)
    .card(card4)
    .card(card5)
    .build();
```

**3. Use Parameterized Tests**
```java
@ParameterizedTest
@ValueSource(strings = {"AH KD 3C TD", "AH", "AH KD 3C TD 9S 2H"})
@DisplayName("Given wrong number of cards, when parsing, then fails")
void testWrongNumberOfCards(String handString) {
    assertThatThrownBy(() -> Hand.parse(handString))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("5 cards");
}

@ParameterizedTest
@CsvSource({
    "AH AH 3C TD 9S, duplicate",
    "2H 2H 2H 3C 4D, duplicate"
})
@DisplayName("Given duplicate cards, when parsing, then fails")
void testDuplicates(String handString, String expectedMessage) {
    assertThatThrownBy(() -> Hand.parse(handString))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(expectedMessage);
}
```

**4. Use AssertJ Fluent Assertions**
```java
// For collections
assertThat(hand.getCards()).hasSize(5);

// For exceptions
assertThatThrownBy(() -> Hand.parse("invalid"))
    .isInstanceOf(IllegalArgumentException.class)
    .hasMessageContaining("expected text");

// For boolean checks
assertThat(hand.containsCard(Rank.ACE, Suit.HEARTS)).isTrue();
```

**5. Use Given-When-Then Structure**
```java
@Test
@DisplayName("Given 'AH KD 3C TD 9S', when parsed, then returns 5 cards")
void testName() {
    // Given
    String handString = "AH KD 3C TD 9S";
    
    // When
    Hand hand = Hand.parse(handString);
    
    // Then
    assertThat(hand.getCards()).hasSize(5);
}
```

**6. Use Descriptive Test Names**
- Method names: `givenX_whenY_thenZ()`
- Display names: `@DisplayName("Given [context], when [action], then [outcome]")`

**7. Immutable Objects**
- All fields should be `final`
- Use `List.copyOf()` for defensive copying
- No setters (use builders instead)

## 📁 Project Structure

```
src/
├── main/java/org/example/poker/
│   ├── Card.java          # Single card (rank + suit)
│   ├── Rank.java          # Card ranks enum (2-A)
│   ├── Suit.java          # Card suits enum (C,D,H,S)
│   └── Hand.java          # Collection of 5 cards
└── test/java/org/example/poker/
    ├── ParseAndValidateWellFormedInputTest.java  # ✅ Passing tests
    └── RejectWrongInputsTest.java                # ❌ 3 failing + TODOs
```

## 🎓 Learning Objectives

By completing this kata, you will:
- ✅ Practice **Test-Driven Development (TDD)**
- ✅ Write **parameterized tests** with JUnit 5
- ✅ Use **AssertJ** fluent assertions
- ✅ Apply **Lombok** to reduce boilerplate
- ✅ Implement **builder pattern** for object construction
- ✅ Create **immutable domain objects**
- ✅ Write **behavioral tests** with Given-When-Then
- ✅ Experience both **test-last** and **test-first** TDD

## 📊 Progress Tracking

- [x] Parse well-formed hands
- [x] Reject wrong number of cards
- [x] Reject empty input
- [ ] **Task 1:** Reject duplicate cards (implement validation)
- [ ] **Task 2:** Reject invalid symbols (write tests + implement)
- [ ] **Task 3:** Refactor to Lombok (remove boilerplate)

## 🔄 TDD Workflow

### Task 1: Test-Last TDD (Implementation from Tests)
1. **Red** - Tests are already failing
2. **Green** - Write minimal code to make tests pass
3. **Refactor** - Clean up your implementation

### Task 2: Test-First TDD (Full Cycle)
1. **Red** - Write failing tests first
2. **Green** - Implement minimal code to pass
3. **Refactor** - Clean up both tests and implementation

### Task 3: Refactoring Discipline
1. **Green** - Ensure all tests pass before refactoring
2. **Refactor** - Change implementation without changing behavior
3. **Green** - Verify all tests still pass after refactoring

**Key principle:** Tests protect you during refactoring!

## 💡 Tips

- **Run tests frequently** - Get immediate feedback
- **Read the tests** - They tell you what to implement
- **Keep it simple** - Don't over-engineer
- **Use the hints** - TODOs provide guidance
- **Follow the patterns** - Look at existing tests for examples

## 🆘 Common Issues

**Tests still failing after implementation?**
- Check your error message contains the expected text
- Verify you're throwing `IllegalArgumentException`
- Make sure validation runs for all cases

**How to run specific tests?**
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests RejectWrongInputsTest

# Run with more details
./gradlew test --info
```

**Import errors?**
- Make sure to add necessary imports (`HashSet`, `Set`, etc.)
- Lombok generates code at compile time - rebuild if needed

## 🎯 Success Criteria

You've completed the kata when:
- ✅ **Task 1:** All tests passing (duplicate validation implemented)
- ✅ **Task 2:** Tests written and implementation complete (invalid symbols)
- ✅ **Task 3:** All boilerplate removed, Lombok annotations applied
- ✅ Code is clean and follows all technical requirements
- ✅ Tests still pass after Task 3 refactoring
- ✅ No compilation errors or warnings

---

**Ready to start?** Run `./gradlew test` and begin with Task 1! 🚀
