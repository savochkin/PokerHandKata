# Hexagonal Architecture Kata - Poker Hand Comparison

A hands-on kata for learning hexagonal architecture (Ports & Adapters) by building a poker hand comparison service with multiple adapters.

## 📖 Business Context

**Gaming Platform Startup - MVP Requirements:**

> "We're building a social poker platform. For our MVP, players need to submit their hands through our web app and see who wins. Later, we'll add a CLI for developers, store game history in a database, and let users view their comparison history."

## 🎓 Learning Objectives

By completing this kata, you will:
- ✅ Understand hexagonal architecture structure
- ✅ Learn to separate domain from infrastructure
- ✅ Practice defining ports (contracts)
- ✅ Implement multiple adapters for same port
- ✅ See how domain stays pure and testable
- ✅ Experience dependency inversion in practice

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

## 📋 Use Cases

### ✅ Use Case 1: Compare Hands via Web Interface (IMPLEMENTED)

**Business Value:** Players can submit hands through web app and see who wins.

**Architecture Flow:**
```
HTTP Request (JSON)
    ↓
RestCompareHandsController (Adapter - IN)
    ↓ calls
CompareHandsUseCase (Port - IN)
    ↓ implemented by
CompareHandsService (Application Service)
    ↓ uses
Hand.compare() (Domain Logic)
    ↓ returns
ComparisonResult (Domain Object)
    ↓ mapped by adapter to
CompareHandsResponse (REST DTO)
    ↓
HTTP Response (JSON)
```

**Key Components:**

1. **Inbound Port:** `CompareHandsUseCase`
   - Interface defining the use case
   - Domain-centric contract
   - No HTTP/REST concepts

2. **Application Service:** `CompareHandsService`
   - Implements the use case
   - Orchestrates domain logic
   - Returns domain objects directly (no DTO mapping at this layer)

3. **REST Adapter:** `RestCompareHandsController`
   - Translates HTTP requests to use case calls
   - Maps domain objects (`ComparisonResult`) to REST DTOs (`CompareHandsResponse`)
   - Handles REST-specific concerns (status codes, JSON, error handling)
   - Depends on port, not on domain directly

**Testing:**
- `CompareHandsUseCaseTest` - Use case tests (107 tests, no Spring)
- `RestCompareHandsControllerTest` - Integration test (with Spring)

**How to Run:**
```bash
# Start the application
./mvnw.sh spring-boot:run

# Test with curl
curl -X POST http://localhost:8080/api/poker/compare \
  -H "Content-Type: application/json" \
  -d '{
    "black": "AH KD 9C 7D 4S",
    "white": "KH QD 9C 7D 4S"
  }'

# Expected response:
{
  "winner": "BLACK",
  "description": "Black wins - high card: Ace"
}
```

---

### ✅ Task 2: Compare Hands via CLI (IMPLEMENTED)

**Business Need:** Developers need command-line access for testing.

**What Was Built:**
- ✅ CLI adapter using Spring Shell (`CliCompareHandsAdapter`)
- ✅ Reuses existing `CompareHandsUseCase` port
- ✅ REST and CLI work simultaneously

**How to Use:**
```bash
# Start app
./mvnw.sh spring-boot:run

# Use CLI
shell:> compare "AH KD 9C 7D 4S" "KH QD 9C 7D 4S"
Black wins - high card: Ace

# REST still works simultaneously
curl -X POST http://localhost:8080/api/poker/compare ...
```

**Files Created:**
- `adapters/in/cli/CliCompareHandsAdapter.java`
- `adapters/in/cli/CliCompareHandsAdapterTest.java`

**Dependencies Added:**
```xml
<dependency>
    <groupId>org.springframework.shell</groupId>
    <artifactId>spring-shell-starter</artifactId>
    <version>3.2.0</version>
</dependency>
```

**Architecture Insight Demonstrated:** 
- ✅ Same service, different adapter
- ✅ Domain doesn't know about CLI vs REST
- ✅ Adapter interchangeability in action

---

### ✅ Task 3: Store Comparison History (IMPLEMENTED)

**Business Need:** Store all comparisons for analytics and auditing.

**What Was Built:**
- ✅ Outbound port: `ComparisonHistoryRepository` interface
- ✅ Domain object: `ComparisonHistoryEntry`
- ✅ Adapter: `InMemoryComparisonHistoryRepository` implementation
- ✅ Service modified to save each comparison

**Architecture:**
```
CompareHandsService
    ↓ (uses)
ComparisonHistoryRepository (Port - defines what domain needs)
    ↑ (implements)
InMemoryComparisonHistoryRepository (Adapter - provides implementation)
```

**Files Created:**
- `app/domain/ComparisonHistoryEntry.java` (domain object)
- `app/port/out/ComparisonHistoryRepository.java` (outbound port)
- `adapters/out/persistence/InMemoryComparisonHistoryRepository.java` (adapter)
- `adapters/out/persistence/InMemoryComparisonHistoryRepositoryTest.java` (tests)

**Modified:**
- `app/service/CompareHandsService.java` - now injects and uses repository

**Architecture Insights Demonstrated:**
- ✅ Domain defines what it needs (port)
- ✅ Adapter provides implementation
- ✅ Easy to swap in-memory → database later
- ✅ Domain stays pure (no persistence logic)
- ✅ Dependency inversion in action

---

### 🔨 Task 4: View Comparison History (20 min)

**Business Need:** Users want to see their past comparisons.

**What to Build:**
- Inbound port: `GetComparisonHistoryUseCase`
- Service: `GetComparisonHistoryService`
- REST endpoint: `GET /api/poker/history`

**Acceptance Criteria:**
```bash
# Compare some hands first
curl -X POST http://localhost:8080/api/poker/compare ...

# View history
curl http://localhost:8080/api/poker/history

# Response:
[
  {
    "id": "uuid",
    "blackHand": "AH KD 9C 7D 4S",
    "whiteHand": "KH QD 9C 7D 4S",
    "winner": "BLACK",
    "description": "Black wins - high card: Ace",
    "timestamp": "2025-11-06T12:00:00"
  }
]
```

**Files to Create:**
- `app/port/in/GetComparisonHistoryUseCase.java`
- `app/service/GetComparisonHistoryService.java`
- `adapters/in/rest/HistoryController.java`

**Architecture Insight:**
- New use case, new port
- Reuses existing outbound port (repository)
- Shows how multiple use cases share adapters

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

### 1. Study the Reference Implementation

```bash
# Run tests to see it works
./mvnw.sh test

# Start the application
./mvnw.sh spring-boot:run

# Test REST API
curl -X POST http://localhost:8080/api/poker/compare \
  -H "Content-Type: application/json" \
  -d '{"black": "AH KD 9C 7D 4S", "white": "KH QD 9C 7D 4S"}'
```

### 2. Explore the Code

**Start with:**
1. `app/port/in/CompareHandsUseCase.java` - The port definition
2. `app/service/CompareHandsService.java` - The service implementation
3. `adapters/in/rest/RestCompareHandsController.java` - The REST adapter

**Notice:**
- Port is just an interface (no implementation details)
- Service depends on port, not on adapter
- Adapter translates HTTP → port calls
- Domain (`Hand.java`) has no Spring annotations

### 3. Complete Tasks in Order

Start with Task 2 (CLI), then Task 3 (persistence), then Task 4 (history view).

Each task builds on the previous one and reinforces hexagonal architecture concepts.

---

## 💡 Tips

### For Task 2 (CLI):
- Use `@ShellComponent` annotation
- Use `@ShellMethod` for commands
- Inject `CompareHandsUseCase` (same as REST controller does)
- CLI and REST both use the same service!

### For Task 3 (Persistence):
- Start with the port interface (what does domain need?)
- Keep it simple: `save()` and `findAll()`
- Use `Map<UUID, Entry>` for in-memory storage
- Service should depend on port, not implementation

### For Task 4 (History):
- New use case = new inbound port
- Reuse outbound port from Task 3
- Service calls repository through port
- REST controller calls service through port

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

## ✅ Success Criteria

Current Progress:

- ✅ All tests pass (120 tests)
- ✅ REST API works for comparison
- ✅ CLI works for comparison (both simultaneously)
- ✅ Comparisons are saved to history
- ⏳ History can be retrieved via REST API (Task 4 - TODO)
- ✅ Domain layer has no framework dependencies
- ✅ Ports define clear contracts
- ✅ Adapters are interchangeable

**Tasks Completed: 3/4**

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

**Ready to start? Begin with studying Use Case 1, then implement Task 2!** 🚀
