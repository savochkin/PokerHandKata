# Hexagonal Architecture Kata - Poker Hand Comparison

A hands-on kata for learning hexagonal architecture (Ports & Adapters) by studying a complete reference implementation, then extending it.

## 📖 What You'll Do

**Phase 1: Study** - Explore a complete, working poker hand comparison service built with hexagonal architecture  
**Phase 2: Extend** - Replace the in-memory persistence adapter with a real database (PostgreSQL, MongoDB, etc.)

## 🎓 Learning Objectives

By completing this kata, you will:
- ✅ See how domain stays pure and separated from infrastructure (and how this is achieved)
- ✅ Learn how ports define clear contracts between the application and the infrastructure
- ✅ Experience swapping adapters without touching domain/services
- ✅ Understand why this architecture makes systems flexible and testable

## 🏗️ Architecture Overview

This project demonstrates **Hexagonal Architecture** (Ports & Adapters):

```
                    ┌─────────────────────────────────────────┐
                    │      INBOUND ADAPTERS (Driving)         │
                    │                                         │
                    │  ┌──────────────┐   ┌──────────────┐   │
                    │  │ REST API     │   │     CLI      │   │
                    │  │ Controller   │   │   Adapter    │   │
                    │  └──────┬───────┘   └──────┬───────┘   │
                    └─────────┼──────────────────┼───────────┘
                              │                  │
                              ▼                  ▼
                    ┌─────────────────────────────────────────┐
                    │         INBOUND PORTS (Use Cases)       │
                    │                                         │
                    │    CompareHandsUseCase                  │
                    │    GetComparisonHistoryUseCase          │
                    └─────────────────┬───────────────────────┘
                                      │
                    ╔═════════════════╧═════════════════╗
                    ║                                   ║
                    ║         APPLICATION               ║
                    ║                                   ║
                    ║    ┌─────────────────────┐        ║
                    ║    │   Domain Model      │        ║
                    ║    │                     │        ║
                    ║    │  Hand, Card, Rank   │        ║
                    ║    │  ComparisonResult   │        ║
                    ║    │  Category, Winner   │        ║
                    ║    └─────────────────────┘        ║
                    ║                                   ║
                    ║    ┌─────────────────────┐        ║
                    ║    │   Services          │        ║
                    ║    │                     │        ║
                    ║    │  CompareHandsService│        ║
                    ║    │  (implements both   │        ║
                    ║    │   use cases)        │        ║
                    ║    └─────────────────────┘        ║
                    ║                                   ║
                    ╚═════════════════╤═════════════════╝
                                      │
                    ┌─────────────────┴───────────────────────┐
                    │      OUTBOUND PORTS (Dependencies)      │
                    │                                         │
                    │    ComparisonHistoryRepository          │
                    └─────────────────┬───────────────────────┘
                              │                  
                              ▼                  
                    ┌─────────────────────────────────────────┐
                    │     OUTBOUND ADAPTERS (Driven)          │
                    │                                         │
                    │  ┌──────────────────────────────────┐   │
                    │  │  InMemoryDB                      │   │
                    │  │  (can be swapped for PostgreSQL) │   │
                    │  └──────────────────────────────────┘   │
                    └─────────────────────────────────────────┘
```

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
│   │   ├── Winner.java
│   │   └── HandComparisonService.java  # Domain service (NO Spring)
│   ├── port/
│   │   ├── in/                      # Inbound ports (use cases)
│   │   │   ├── CompareHandsUseCase.java
│   │   │   └── GetComparisonHistoryUseCase.java
│   │   └── out/                     # Outbound ports (dependencies)
│   │       └── ComparisonHistoryRepository.java
│   └── service/
│       └── CompareHandsService.java         # Application service (thin layer)
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
│       └── inmemorydb/              # In-memory DB adapter (IMPLEMENTED)
│           └── InMemoryDB.java
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
1. Would the domain model be affected if we need to migrate from Spring to Quarkus?
2. Can you test the comparison logic without starting Spring?
3. Check if a database is used to test the domain service implementing the business requirement that every comparison should be saved in history?
4. What would happen if we decide to store the history in a different database? Would we need to change any tests? Would we need to change the domain service? 
5. Notice that the HandComparisonService does not depend on the real database. How was this achieved?
6. Explain how the dependency inversion works here for HandComparisonService and the actual DB used in the app?

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
1. What drives the inbound ports? Are they driven by what is needed by the presentation level (UI) or the domain? 
2. Why inbound ports are often called use cases?
3. Are we required to always have 1-1 mapping between inboudn port (usecase) and the app service implementing it?
2. Notice that we have two inbound ports (use cases) but only one app service implementing them. Can we also choose to split it into two - one per interface? 
3. What if we decide that our app service is too complex and decide to split it into two? Would that affect our clients (REST and CLI adapters)?

---

### Part 3: Understand Application Service (Use Case Orchestration)

**Files to examine:**
- `app/service/CompareHandsService.java`

**What to notice:**
- ✅ Implements both inbound ports (`CompareHandsUseCase` and `GetComparisonHistoryUseCase`)
- ✅ Delegates to domain service (`HandComparisonService`)
- ✅ Thin layer - just wiring and delegation
- ✅ Returns domain objects (no DTO mapping here)

**Understanding Check:**
1. What does `CompareHandsService` depend on?
2. Would this service be affected if we decide to migrate from Spring to Quarkus?
3. What is the responsibility of this service?
4. What is the difference between a domain service and an app service?

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
1. Who defines this interface - the domain or the external system or the adapter?
2. Suppose we use an external system 'ExternalSystem' for both saving historical records and integrating with a leaderboard. 
Should we use 'ExternalSystemPort' with many operations, or is it better to use a 'HistoricalPort' and a 'LeaderboardPort'?
3. What if we later decide to use another system for the leaderboard? 
4. What if the external system we use has a much more complex domain model - do we need to mirror it in our domain model?
5. What if the external DB has a DTO class - HistoricalRecord. Can we use it in our adapter? 
6. How would this affect us if we would like to migrate to another DB later?
7. What would you need to change to swap in-memory storage for PostgreSQL?

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
5. What do we usually test when testing an inbound adapter?

---

### Part 6: Understand Outbound Adapters (Driven by the Application)

**Files to examine:**
- `adapters/out/inmemorydb/InMemoryDB.java`

**What to notice:**
- ✅ Implements outbound port
- ✅ Contains all technical details (Map, UUID generation)
- ✅ Domain doesn't know about this implementation
- ✅ Easy to swap for different implementation

**Understanding Check:**
1. What interface does this implement?
2. If we need to use an external system to store the history, what would we need to do?
3. What if the external system client migrates from REST to GraphQL?
4. Would the domain or service need to change in the above case?
5. What do we usually test when we are testing an outbound adapter?



---

### Part 7: Run and Experiment

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
---

### ✅ Task 1 Complete When:

- [ ] You can explain the flow from HTTP request to domain and back
- [ ] You understand why domain has no framework dependencies
- [ ] You can identify which files would change for different scenarios
- [ ] You can answer all understanding check questions
- [ ] You've run both REST and CLI and seen them share the same service

**Next:** Task 2 - Extend the system with a real database adapter

TODO

## 📖 Further Reading

- **Hexagonal Architecture** by Alistair Cockburn
- **Clean Architecture** by Robert C. Martin
- **Domain-Driven Design** by Eric Evans
- **Growing Object-Oriented Software, Guided by Tests** by Freeman & Pryce

---

**Ready to start? Begin with Task 1 to understand the complete implementation!** 🚀
