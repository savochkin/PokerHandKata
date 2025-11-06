# Hexagonal Architecture Kata - Poker Hand Comparison

A hands-on kata for learning hexagonal architecture (Ports & Adapters) by studying a complete reference implementation, then extending it.

## 📖 What You'll Do

**Phase 1: Study** - Explore a complete, working poker hand comparison service built with hexagonal architecture  
**Phase 2: Extend** - Replace the in-memory persistence adapter with a real database (PostgreSQL, MongoDB, etc.)

## 🎓 Learning Objectives

By completing this kata, you will:
- ✅ Understand hexagonal architecture structure by examining real code
- ✅ See how domain stays pure and separated from infrastructure
- ✅ Learn how ports define clear contracts between layers
- ✅ Experience swapping adapters without touching domain/services
- ✅ Practice dependency inversion in a real system
- ✅ Understand why this architecture makes systems flexible and testable

## 🏗️ Architecture Overview

This project demonstrates **Hexagonal Architecture** (Ports & Adapters):

### Key Principles

1. **Domain at the center** - Pure business logic with no external dependencies
2. **Ports define contracts** - Interfaces that express what the domain needs/provides
3. **Adapters connect to outside world** - REST APIs, CLI, databases, etc.
4. **Dependency inversion** - Domain doesn't depend on adapters; adapters depend on domain

### Folder Structure

```
src/main/java/org/example/poker/
├── app/                             # INSIDE THE HEXAGON
│   ├── domain/                      # Pure business logic
│   │   ├── Hand.java
│   │   ├── Card.java, Rank.java, Suit.java
│   │   ├── Category.java
│   │   ├── ComparisonResult.java
│   │   └── Winner.java
│   ├── port/
│   │   ├── in/                      # Inbound ports (use cases)
│   │   │   └── CompareHandsUseCase.java
│   │   └── out/                     # Outbound ports (dependencies)
│   │       └── ComparisonHistoryRepository.java (IMPLEMENTED)
│   └── service/
│       └── CompareHandsService.java # Application service
│
├── adapters/                        # OUTSIDE THE HEXAGON
│   ├── in/                          # Inbound adapters (driving)
│   │   ├── rest/                    # REST API adapter (IMPLEMENTED)
│   │   │   ├── RestCompareHandsController.java
│   │   │   ├── CompareHandsRequest.java (REST input DTO)
│   │   │   └── CompareHandsResponse.java (REST output DTO)
│   │   └── cli/                     # CLI adapter (IMPLEMENTED)
│   │       └── CliCompareHandsAdapter.java
│   └── out/                         # Outbound adapters (driven)
│       └── persistence/             # Persistence adapter (IMPLEMENTED)
│           └── InMemoryComparisonHistoryRepository.java
│
└── PokerApplication.java            # Spring Boot main class
```

## 🎯 Task 1: Review & Understand the Implementation (60-90 min)

**Goal:** Understand how hexagonal architecture works by exploring a complete, working implementation.

### Part 1: Explore the Domain (Pure Business Logic)

**Files to examine:**
- `app/domain/Hand.java`
- `app/domain/Card.java`, `Rank.java`, `Suit.java`
- `app/domain/Category.java`
- `app/domain/ComparisonResult.java`

**What to notice:**
- ✅ No Spring annotations (`@Component`, `@Service`, etc.)
- ✅ No external dependencies (no HTTP, no database concepts)
- ✅ Pure Java - could work in any context
- ✅ Business rules are explicit and testable

**Understanding Check:**
1. Why does `Hand.java` have no Spring annotations?
2. What would happen if we needed to change from Spring to Quarkus?
3. Can you test `Hand.compare()` without starting Spring?

---

### Part 2: Understand Inbound Ports (What the Application Provides)

**Files to examine:**
- `app/port/in/CompareHandsUseCase.java`
- `app/port/in/GetComparisonHistoryUseCase.java`

**What to notice:**
- ✅ Just interfaces - no implementation
- ✅ Use domain objects as input/output (not DTOs)
- ✅ Express business capabilities, not technical details
- ✅ No mention of HTTP, JSON, or REST

**Understanding Check:**
1. Why are these interfaces and not classes?
2. What's the difference between a port and a service?
3. Could a CLI adapter use `CompareHandsUseCase`? How?

---

### Part 3: Understand Application Services (Use Case Orchestration)

**Files to examine:**
- `app/service/CompareHandsService.java`
- `app/service/GetComparisonHistoryService.java`

**What to notice:**
- ✅ Implements inbound ports
- ✅ Orchestrates domain logic
- ✅ Uses outbound ports (dependencies)
- ✅ Returns domain objects (no DTO mapping here)

**Understanding Check:**
1. What does `CompareHandsService` depend on?
2. Why does it depend on `ComparisonHistoryRepository` interface, not the concrete implementation?
3. Where does the actual hand comparison logic live?

---

### Part 4: Understand Outbound Ports (What the Application Needs)

**Files to examine:**
- `app/port/out/ComparisonHistoryRepository.java`

**What to notice:**
- ✅ Interface defining what domain needs
- ✅ Domain defines the contract, not the adapter
- ✅ No implementation details (in-memory vs database)
- ✅ Uses domain objects (`ComparisonHistoryEntry`)

**Understanding Check:**
1. Who defines this interface - the domain or the adapter?
2. Why is this better than the service directly using `InMemoryComparisonHistoryRepository`?
3. What would you need to change to swap in-memory storage for PostgreSQL?

---

### Part 5: Understand Inbound Adapters (Driving the Application)

**Files to examine:**
- `adapters/in/rest/RestCompareHandsController.java`
- `adapters/in/rest/CompareHandsRequest.java` / `CompareHandsResponse.java`
- `adapters/in/rest/HistoryController.java`
- `adapters/in/cli/CliCompareHandsAdapter.java`

**What to notice:**
- ✅ Depend on ports, not services directly
- ✅ Translate external format (JSON/CLI) to domain calls
- ✅ Map domain objects to external DTOs
- ✅ Handle adapter-specific concerns (HTTP status codes, error handling)
- ✅ Multiple adapters use the same ports

**Understanding Check:**
1. Why does `RestCompareHandsController` inject `CompareHandsUseCase` instead of `CompareHandsService`?
2. Where does JSON-to-domain mapping happen?
3. How can REST and CLI both work simultaneously?
4. What would change if you added a GraphQL adapter?

---

### Part 6: Understand Outbound Adapters (Driven by the Application)

**Files to examine:**
- `adapters/out/persistence/InMemoryComparisonHistoryRepository.java`

**What to notice:**
- ✅ Implements outbound port
- ✅ Contains all technical details (Map, UUID generation)
- ✅ Domain doesn't know about this implementation
- ✅ Easy to swap for different implementation

**Understanding Check:**
1. What interface does this implement?
2. Why use a `Map<String, ComparisonHistoryEntry>` instead of a real database?
3. What files would you need to change to add PostgreSQL support?
4. Would the domain or service need to change?

---

### Part 7: Trace a Complete Request (End-to-End)

**Exercise:** Trace what happens when you call:
```bash
curl -X POST http://localhost:8080/api/poker/compare \
  -H "Content-Type: application/json" \
  -d '{"black": "AH KD 9C 7D 4S", "white": "KH QD 9C 7D 4S"}'
```

**Step-by-step flow:**
1. **HTTP Request arrives** → Spring receives JSON
2. **REST Adapter** (`RestCompareHandsController`)
   - Deserializes JSON to `CompareHandsRequest` (DTO)
   - Calls `compareHandsUseCase.compare(black, white)`
3. **Inbound Port** (`CompareHandsUseCase`)
   - Just an interface - routes to implementation
4. **Application Service** (`CompareHandsService`)
   - Parses hand strings to `Hand` objects (domain)
   - Calls `Hand.compare()` (domain logic)
   - Gets `ComparisonResult` (domain object)
   - Calls `repository.save()` (outbound port)
   - Returns `ComparisonResult`
5. **Outbound Port** (`ComparisonHistoryRepository`)
   - Routes to implementation
6. **Outbound Adapter** (`InMemoryComparisonHistoryRepository`)
   - Stores in `Map<String, Entry>`
7. **Back to REST Adapter**
   - Maps `ComparisonResult` to `CompareHandsResponse` (DTO)
   - Returns JSON with HTTP 200

**Understanding Check:**
1. At which layer does JSON parsing happen?
2. At which layer does business logic (hand comparison) happen?
3. At which layer does persistence happen?
4. Which layers would change if you replaced REST with GraphQL?
5. Which layers would change if you replaced in-memory storage with PostgreSQL?

---

### Part 8: Run and Experiment

**Start the application:**
```bash
./mvnw.sh spring-boot:run
```

**Try the REST API:**
```bash
# Compare hands
curl -X POST http://localhost:8080/api/poker/compare \
  -H "Content-Type: application/json" \
  -d '{"black": "AH KD 9C 7D 4S", "white": "KH QD 9C 7D 4S"}'

# View history
curl http://localhost:8080/api/poker/history
```

**Try the CLI:**
```bash
# In the Spring Shell prompt:
shell:> compare "AH KD 9C 7D 4S" "KH QD 9C 7D 4S"
```

**Understanding Check:**
1. Do both REST and CLI save to the same history?
2. Can you see history entries from both interfaces?
3. What proves that both adapters use the same service?

---

### Part 9: Final Understanding Check

Answer these questions to verify your understanding:

1. **Dependency Direction:**
   - Does the domain depend on adapters, or do adapters depend on the domain?
   - Why is this important?

2. **Adapter Interchangeability:**
   - Can you have multiple inbound adapters for the same use case?
   - Can you have multiple outbound adapters for the same port?
   - Give examples from this codebase.

3. **Testing:**
   - Can you test domain logic without Spring?
   - Can you test services with mock repositories?
   - Where would you write integration tests?

4. **Change Impact:**
   - What would change if you added a mobile app?
   - What would change if you switched from Spring to Micronaut?
   - What would change if you added PostgreSQL?

5. **Boundaries:**
   - What belongs in the domain layer?
   - What belongs in adapters?
   - What belongs in services?

---

### ✅ Task 1 Complete When:

- [ ] You can explain the flow from HTTP request to domain and back
- [ ] You understand why domain has no framework dependencies
- [ ] You can identify which files would change for different scenarios
- [ ] You can answer all understanding check questions
- [ ] You've run both REST and CLI and seen them share the same service

**Next:** Task 2 - Extend the system with a real database adapter

---

## 📚 Key Concepts

### Inbound Ports (Use Cases)
- Define what the application **provides** to the outside world
- Interfaces in `app/port/in/`
- Example: `CompareHandsUseCase`, `GetComparisonHistoryUseCase`

### Outbound Ports (Dependencies)
- Define what the application **needs** from the outside world
- Interfaces in `app/port/out/`
- Example: `ComparisonHistoryRepository`

### Inbound Adapters (Driving)
- Translate external requests into use case calls
- In `adapters/in/`
- Example: REST controllers, CLI, message consumers

### Outbound Adapters (Driven)
- Implement outbound ports
- In `adapters/out/`
- Example: Database repositories, external API clients

### Domain
- Pure business logic
- No framework dependencies
- Example: `Hand`, `Card`, comparison logic

### Application Services
- Orchestrate use cases
- Implement inbound ports
- Use outbound ports
- In `app/service/`

---

## 🚀 Getting Started

### Step 1: Verify the Implementation Works

```bash
# Run tests to see everything works
./mvnw.sh test

# Start the application
./mvnw.sh spring-boot:run

# Test REST API
curl -X POST http://localhost:8080/api/poker/compare \
  -H "Content-Type: application/json" \
  -d '{"black": "AH KD 9C 7D 4S", "white": "KH QD 9C 7D 4S"}'

# View history
curl http://localhost:8080/api/poker/history
```

### Step 2: Complete Task 1 (Study Guide)

Follow the **Task 1** section above to systematically explore:
- Domain layer (pure business logic)
- Ports (contracts)
- Services (orchestration)
- Adapters (infrastructure)
- Complete request flow

Answer all "Understanding Check" questions as you go.

### Step 3: Complete Task 2 (Extension)

Once you understand the architecture, extend the system by implementing a real database adapter (PostgreSQL, MongoDB, etc.) to replace the in-memory storage.

This will demonstrate:
- How easy it is to swap adapters
- Why ports matter
- That domain and services don't change

---

## 🎯 Hexagonal Architecture Benefits

### 1. Technology Independence
- Domain logic (Hand comparison) has zero dependencies on Spring, HTTP, or databases
- Can test domain without any infrastructure
- Can swap REST for GraphQL without touching domain

### 2. Testability
- Domain tests: Pure unit tests, fast, no mocks needed
- Service tests: Test with mock ports
- Adapter tests: Integration tests with real infrastructure

### 3. Flexibility
- Multiple UIs (REST + CLI) using same service
- Easy to add new adapters (mobile app, message queue)
- Easy to swap implementations (in-memory → PostgreSQL)

### 4. Clear Boundaries
- **Domain:** What the system does (business rules)
- **Ports:** How to interact with domain (contracts)
- **Adapters:** Technical details (HTTP, DB, CLI)

### 5. Maintainability
- Changes in REST API don't affect domain
- Changes in database don't affect use cases
- Each layer has single responsibility

---

## 🛠️ Testing Strategy

### Domain Tests (Existing)
```
CompareHighCardHandsTest
ComparePairHandsTest
...
```
- Test pure business logic
- No infrastructure dependencies
- Fast, reliable, focused

### Service Tests
```
CompareHandsServiceTest
```
- Test use case orchestration
- Mock outbound ports if needed
- No Spring context required

### Adapter Tests
```
RestCompareHandsControllerTest
```
- Test complete flow
- Use Spring Boot test context
- Verify HTTP/JSON handling

---

## ✅ Reference Implementation Status

This kata provides a **complete, working implementation** for study:

- ✅ All tests pass (120+ tests)
- ✅ REST API works for comparison
- ✅ CLI works for comparison (both simultaneously)
- ✅ Comparisons are saved to history
- ✅ History can be retrieved via REST API
- ✅ Domain layer has no framework dependencies
- ✅ Ports define clear contracts
- ✅ Adapters are interchangeable

**Your Task:** Study the implementation (Task 1), then extend it (Task 2)

---

## 🤔 Discussion Questions

After completing the kata, discuss:

1. **What would change if we swap in-memory storage for PostgreSQL?**
   - Only the adapter implementation
   - Port stays the same
   - Service stays the same
   - Domain stays the same

2. **How would you add a GraphQL adapter?**
   - Create new adapter in `adapters/in/graphql/`
   - Reuse existing `CompareHandsUseCase` port
   - No changes to service or domain

3. **Why keep domain pure (no Spring annotations)?**
   - Testable without infrastructure
   - Framework-independent
   - Business logic is explicit
   - Can use domain in any context

4. **What's the benefit of ports?**
   - Clear contracts
   - Dependency inversion
   - Adapter interchangeability
   - Testability with mocks

---

## 📖 Further Reading

- **Hexagonal Architecture** by Alistair Cockburn
- **Clean Architecture** by Robert C. Martin
- **Domain-Driven Design** by Eric Evans
- **Growing Object-Oriented Software, Guided by Tests** by Freeman & Pryce

---

**Ready to start? Begin with Task 1 to understand the complete implementation!** 🚀
