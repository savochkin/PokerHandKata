# Behavioral Testing Approach for Poker Hand Kata

## Philosophy
Tests are organized by **user stories** and **acceptance criteria**, not by implementation classes. This makes tests:
- **Living documentation** - Tests read like specifications
- **Business-focused** - Tests validate behavior, not implementation
- **Refactoring-safe** - Tests don't break when you refactor internals
- **Story-driven** - Each test file maps to one story from the kata

## Structure

### File Naming Convention
```
Story{N}_{StoryName}Test.java
```
Examples:
- `Story1_ParseAndValidateCardsTest.java`
- `Story2_RankHighCardHandsTest.java`
- `Story3_CompareTwoHandsTest.java`

### Test Organization Pattern

```java
@DisplayName("Story N: Story Title")
class StoryN_StoryTitleTest {
    
    @Nested
    @DisplayName("Acceptance Criterion: [criterion from story]")
    class DescriptiveContextName {
        
        @Test
        @DisplayName("Given [context], when [action], then [expected outcome]")
        void givenX_whenY_thenZ() {
            // Given - Setup
            
            // When - Action
            
            // Then - Assertions
        }
    }
}
```

## Benefits for Team Kata

### 1. **Clear Story Boundaries**
Each story has its own test file. When rotating pairs:
- Easy to see what's complete
- Clear what to work on next
- No confusion about scope

### 2. **Self-Documenting**
New team members can read tests to understand:
- What the system does
- Why it does it
- What edge cases are covered

### 3. **Acceptance Criteria as Tests**
Each `@Nested` class = one acceptance criterion from the story.
Product owner can review test names to verify completeness.

### 4. **Better Test Reports**
JUnit 5's `@DisplayName` produces readable test reports:
```
Story 1: Parse & Validate Cards
  ✓ Acceptance Criterion: Parse well-formed hand strings
    ✓ Given 'AH KD 3C TD 9S', when parsed, then returns 5 cards...
  ✓ Acceptance Criterion: Reject duplicate cards
    ✓ Given duplicate cards, when parsing, then fails...
```

### 5. **TDD-Friendly**
Write tests with TODOs first (see `Story2_RankHighCardHandsTest.java`):
- Clarifies what to build
- Prevents over-engineering
- Keeps focus on current story

## Test Naming Conventions

### Use Given-When-Then Format
```java
@Test
@DisplayName("Given [precondition], when [action], then [outcome]")
void givenX_whenY_thenZ() { ... }
```

### Method Names
Use underscores for readability:
```java
void givenDuplicateCards_whenParsing_thenFailsWithValidationError()
```

### Be Specific
❌ Bad: `testParsing()`
✅ Good: `givenWellFormedString_whenParsed_thenReturns5CardsWithCorrectRanksAndSuits()`

## Parameterized Tests
Use for multiple examples of same behavior:
```java
@ParameterizedTest
@ValueSource(strings = {"2H 3D 5S 9C KD", "AH KD 3C TD 9S"})
@DisplayName("Given various valid hand formats, when parsed, then successfully creates hand")
void givenVariousValidFormats_whenParsed_thenSuccessfullyCreatesHand(String handString) {
    // ...
}
```

## Example: Story 1 Structure

```
Story1_ParseAndValidateCardsTest
├── ParseWellFormedHands (Acceptance Criterion 1)
│   ├── givenWellFormedString_whenParsed_thenReturns5CardsWithCorrectRanksAndSuits
│   └── givenVariousValidFormats_whenParsed_thenSuccessfullyCreatesHand [4 cases]
├── RejectDuplicateCards (Acceptance Criterion 2)
│   └── givenDuplicateCards_whenParsing_thenFailsWithValidationError [3 cases]
└── RejectMalformedInput (Acceptance Criterion 3)
    ├── givenMalformedTokens_whenParsing_thenFailsWithClearErrorMessage [5 cases]
    ├── givenWrongNumberOfCards_whenParsing_thenFailsWithValidationError [3 cases]
    └── givenEmptyString_whenParsing_thenFailsWithValidationError
```

## Migration Strategy

### For Existing Tests
1. Keep old test files during transition
2. Create new story-based test files
3. Copy/refactor tests into new structure
4. Delete old files when complete

### For New Stories
1. Create `StoryN_TitleTest.java` file
2. Add `@Nested` classes for each acceptance criterion
3. Write tests with TODOs for unimplemented features
4. Implement features to make tests pass

## Tools Used
- **JUnit 5** - `@Nested`, `@DisplayName`, `@ParameterizedTest`
- **AssertJ** - Fluent assertions
- **Given-When-Then** - BDD-style test structure

## Anti-Patterns to Avoid

❌ **Don't test implementation details**
```java
@Test
void testCardConstructor() { ... } // Too low-level
```

✅ **Do test behaviors**
```java
@Test
@DisplayName("Given valid rank and suit, when creating card, then card is usable in hand")
void givenValidRankAndSuit_whenCreatingCard_thenCardIsUsableInHand() { ... }
```

❌ **Don't organize by class**
```java
class CardTest { ... }
class HandTest { ... }
```

✅ **Do organize by story**
```java
class Story1_ParseAndValidateCardsTest { ... }
class Story2_RankHighCardHandsTest { ... }
```

## Running Tests

```bash
# Run all tests
./gradlew test

# Run specific story
./gradlew test --tests Story1_ParseAndValidateCardsTest

# Run with detailed output
./gradlew test --info
```

## Summary

This approach makes tests:
- **Readable** - Anyone can understand what the system does
- **Maintainable** - Tests survive refactoring
- **Valuable** - Tests document business requirements
- **Focused** - Each story is independent and complete
