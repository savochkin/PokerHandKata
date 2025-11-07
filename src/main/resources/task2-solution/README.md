# Task 2 - Reference Solution

This folder contains a **reference solution** for Task 2.

## ⚠️ Important

**Try to implement the adapter yourself first!** Only refer to this solution if you're stuck or want to compare your implementation.

## Files

- `AuditSystemAdapter.java.solution` - Complete adapter implementation
- `AuditSystemAdapterTest.java.solution` - Complete test suite

## How to Use

1. **If you're stuck:**
   - Look at the solution to understand the approach
   - Try to implement it yourself based on what you learned
   - Don't just copy-paste - understand each part

2. **After completing Task 2:**
   - Compare your solution with this reference
   - Look for differences in approach
   - Consider which approach is better and why

## To Use the Solution

1. **Copy the adapter:**
   ```bash
   cp src/main/resources/task2-solution/AuditSystemAdapter.java.solution \
      src/main/java/org/example/poker/adapters/out/auditsystem/AuditSystemAdapter.java
   ```

2. **Copy the tests:**
   ```bash
   cp src/main/resources/task2-solution/AuditSystemAdapterTest.java.solution \
      src/test/java/org/example/poker/adapters/out/auditsystem/AuditSystemAdapterTest.java
   ```

3. **Run tests:**
   ```bash
   ./mvnw.sh test
   ```

## Key Points in the Solution

1. **Adapter implements the port** - `ComparisonHistoryRepository`
2. **Uses external system** - Creates and delegates to `AuditSystem`
3. **Mapping methods** - `toAuditRecord()` and `toDomain()`
4. **ID generation** - Assigns UUID if ID is null
5. **Clean separation** - Domain knows nothing about external system

## Learning from the Solution

- Notice how mapping logic is isolated in private methods
- See how enums are converted using `.name()` and `valueOf()`
- Observe how nullable fields are handled
- Understand the clear separation of concerns

Remember: The goal is to **learn hexagonal architecture**, not just to complete the task!
