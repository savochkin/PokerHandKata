---
trigger: always_on
description: 
globs: 
---

# Rule: TDD first

**Description:**  
Always drive development through tests. For both features and bugs, start by writing a failing test, review it, then implement the fix or feature. Tests must be readable, maintainable, independent, and non-redundant, using AssertJ and parameterized tests when they add clarity.

## Activation
- Applies to: **all coding tasks**
- Behavior: **Write tests first; do not implement until I approve the test.**

## When implementing a new feature
1. **Start with a failing test** that uses the new public classes, components, or ports.
2. The test must fail for the right reason (clear assertion message).
3. **Stop here** — I’ll review the test before you start coding.

## When fixing a bug
1. **Reproduce with a failing test** that captures the minimal failing scenario.
2. Verify the test fails and documents the bug clearly.
3. **Stop here** — I’ll review before the fix implementation.

## Test style & tooling
- **Assertions:** Use **AssertJ** for fluent, expressive checks.
- **Readability first:** Tests must be concise, clear, and intention-revealing.
- **Independence:** Each test should stand alone; no hidden dependencies or shared mutable state.
- **Parameterized tests:** Prefer them to reduce duplication and make input–output relations explicit.
- **No duplicates:** Minimize overlapping tests covering the same functionality; fewer high-value tests are better.
- **Public behavior only:** Test via public APIs or ports, not private implementation details.
- **Representative coverage:** Include happy path, edge, and failure cases—avoid repetitive permutations.
- **Names:** Use behavior-focused names like `shouldRejectExpiredToken()`.

## Choosing test types
- Use **mocks** when isolation and independence are critical.
- Use **stubs/spies** when realistic data flows improve clarity or test setup is complex.
- Avoid infrastructure dependencies in unit tests; keep them pure.

## Guardrails
- No production code before a failing test exists and is reviewed.
- If two tests fail for the same root cause, merge or remove one.
- Keep fixtures small; use builders/helpers for clarity.
- Flaky tests must be stabilized or removed quickly.
- Regularly prune redundant or outdated tests.

## Minimal checklist before coding
- [ ] Failing test exists and captures desired behavior or bug.
- [ ] Test is readable, independent, and uses AssertJ.
- [ ] Parameterized where it reduces duplication.
- [ ] No redundant coverage; similar cases consolidated.
- [ ] Agreed on mock vs stub/spy approach.
- [ ] Test reviewed and approved before coding.