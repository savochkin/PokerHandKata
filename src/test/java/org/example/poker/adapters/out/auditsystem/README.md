# Task 2: Implement Audit System Adapter

This folder is where you will implement your `AuditSystemAdapter`.

## What to create:

**File:** `AuditSystemAdapter.java`

**Purpose:** Integrate your poker comparison application with the external Audit System.

## Requirements:

1. Implement `ComparisonHistoryRepository` port
2. Use the provided `AuditSystem` (in `externalsystems/auditsystem/`)
3. Map between your domain model and the external system's model:
   - `ComparisonHistoryEntry` ↔ `AuditRecord`
   - `ComparisonResult` ↔ `ComparisonOutcome`
4. Handle ID generation for new entries

## Optional - Tests:

If you want to write tests, a template is available in:
`src/main/resources/task2-solution/AuditSystemAdapterTest.java.solution`

You can copy it here and use it to verify your implementation.

## Key Points:

- The external system has its own data model - you cannot change it
- All mapping logic belongs in the adapter
- Keep your domain pure - no external system types in domain
- The adapter is the bridge between your application and the external system

## See:

Refer to the main README.md for detailed instructions (Task 2).
