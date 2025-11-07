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
                    │  │  AuditSharedDB                   │   │
                    │  │  (maps to shared DB)             │   │
                    │  └──────────────┬───────────────────┘   │
                    └─────────────────┼───────────────────────┘
                                      ▼
                    ┌─────────────────────────────────────────┐
                    │     EXTERNAL SYSTEMS                     │
                    │                                         │
                    │  ┌──────────────────────────────────┐   │
                    │  │  AuditSharedDBClient             │   │
                    │  │  (shared database)               │   │
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
│   │   │   ├── CompareHandsRequest.java (REST input DTO)
│   │   │   └── CompareHandsResponse.java (REST output DTO)
│   │   └── cli/                     # CLI adapter (IMPLEMENTED)
│   │       └── CliCompareHandsAdapter.java
│   └── out/                         # Outbound adapters (driven)
│       ├── auditshareddb/           # Shared audit DB adapter (IMPLEMENTED)
│       │   └── AuditSharedDB.java
│       └── auditsystem/             # Audit system adapter (TASK 2: TO IMPLEMENT)
│           └── (You will create AuditSystemAdapter.java here)
│
├── externalsystems/                 # EXTERNAL SYSTEMS (outside our control)
│   ├── auditdb/                     # Shared audit database (Task 1)
│   │   └── AuditSharedDBClient.java # Database client
│   └── auditsystem/                 # External audit system (Task 2)
│       ├── AuditSystem.java         # External system API
│       ├── AuditRecord.java         # External system data model
│       └── ComparisonOutcome.java   # External system data model
│
└── PokerApplication.java            # Spring Boot main class
```

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

### Part 3: Understand Application Service 

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
5. What if the external DB has a DTO class - AuditRecord. Can we use it in our adapter? 
6. How would this affect us if we are forced to migrate from a shared DB to an API when communicating with an AuditSystem?

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
1. Why does `RestCompareHandsController` inject `CompareHandsUseCase` instead of `CompareHandsService`? Can we even make CompareHandsService not public?
2. Where does JSON-to-domain mapping happen?
3. How can REST and CLI both work simultaneously?
4. What would change if you added a GraphQL adapter?
5. What do we usually test when testing an inbound adapter?

---

### Part 6: Understand Outbound Adapters (Driven by the Application)

**Files to examine:**
- `adapters/out/auditshareddb/AuditSharedDB.java`
- `externalsystems/auditdb/AuditSharedDBClient.java`

**What to notice:**
- ✅ Adapter implements outbound port
- ✅ Adapter delegates to external system client (`AuditSharedDBClient`)
- ✅ Adapter maps between domain objects and database format (key-value maps)
- ✅ Domain doesn't know about the database or its format
- ✅ External system is clearly separated

**Understanding Check:**
1. What interface does the adapter implement?
2. What is the role of `AuditSharedDBClient`? Is it part of our application?
3. Why does the adapter need mapping methods (`toDatabaseRecord`, `toDomain`)?
4. If we need to use a different external system, what would we need to do?
5. Would the domain or service need to change if we swap external systems?

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

### ✅ Task 1 Complete When:

- [ ] You can explain the flow from HTTP request to domain and back
- [ ] You understand why domain has no framework dependencies
- [ ] You can identify which files would change for different scenarios
- [ ] You can answer all understanding check questions
- [ ] You've run both REST and CLI and seen them share the same service

---

## 🎯 Task 2: Implement an Adapter for External Audit System (60-90 min)

**Goal:** Apply what you learned by implementing an adapter that integrates with an external system.

### Background

**The situation:**

Initially, your poker comparison application stored history in a **shared audit database** (the `AuditSharedDB` adapter you explored in Task 1). This worked fine for initial requirements.

However, **requirements changed**:
- Initially, you were asked to store history in a **shared Audit Database**
- But the Compliance Team decided to **move away from shared databases** to a microservices architecture.
- Now you must use the **Audit System API** instead

**The Audit System:**
- Maintained by a different team (Compliance team)
- Has its own data model (`AuditRecord`, `ComparisonOutcome`)
- Provides a Java API (`AuditSystem`)
- **You cannot change this system** - you must adapt to it
- Their model is **different** from your domain model

**The challenge:**
- External system model: `AuditRecord` with `ComparisonOutcome`
- Your domain model: `ComparisonHistoryEntry` with `ComparisonResult`
- You need to bridge this gap **without changing your domain**

**Your task:** Create an adapter that allows your poker application to store comparison history using the Audit System API, while keeping your domain pure and unchanged.

---

### Part 1: Understand the External System

**Files to examine:**
- `externalsystems/auditsystem/AuditSystem.java` - External system API
- `externalsystems/auditsystem/AuditRecord.java` - External system data model
- `externalsystems/auditsystem/ComparisonOutcome.java` - External system data model

**Compare with Task 1:**
- Task 1: `AuditSharedDB` adapter → `AuditSharedDBClient` (Map<String, String>)
- Task 2: `AuditSystemAdapter` → `AuditSystem` (AuditRecord)
- **Same pattern, different external systems!**

**Key observations:**
1. The external system has its own data model (not our domain model)
2. We must use their API: `storeRecord()`, `retrieveAllRecords()`
3. We must map between our domain and their model
4. This is a **realistic scenario** - external systems rarely match our domain
5. Notice the **parallel structure** with `AuditSharedDB` from Task 1
---

### Part 2: Implement the Adapter

**Create:** `adapters/out/auditsystem/AuditSystemAdapter.java`

**Requirements:**

1. **Implement the port interface:**
   ```java
   public class AuditSystemAdapter implements ComparisonHistoryRepository {
       // Your implementation
   }
   ```

2. **Use the external system:**
   - Create an instance of `AuditSystem`
   - Delegate storage operations to it
   - Use its API (`storeRecord()`, `retrieveAllRecords()`)

3. **Map between models:**
   - Domain → External: `ComparisonHistoryEntry` → `AuditRecord`
   - External → Domain: `AuditRecord` → `ComparisonHistoryEntry`
   - Handle nested objects: `ComparisonResult` ↔ `ComparisonOutcome`

4. **Handle IDs:**
   - Generate UUID for new entries (if ID is null)
   - This is a domain rule: repository assigns IDs

5. **Constructor:**
   ```java
   public AuditSystemAdapter() {
       // Initialize AuditSystem
   }
   
   // Optional: Constructor for testing with specific instance
   public AuditSystemAdapter(AuditSystem auditSystem) {
       this.auditSystem = auditSystem;
   }
   ```

**Hints:**
- The adapter is responsible for ALL mapping logic
- Keep domain pure - no external system types in domain
- Map enums using `.name()` and `valueOf()`
- Handle nullable fields (winningRank, losingRank)
- Look at `AuditSharedDB` for reference - very similar structure!

**Optional - Write Tests:**
- A test template is available in `src/main/resources/task2-solution/AuditSystemAdapterTest.java.solution`
- Copy it to `src/test/java/.../auditsystem/` if you want to use tests

---

### Part 3: Verify Integration

**Run tests:**
```bash
./mvnw.sh test
```

**Expected:**
- All existing tests still pass (domain, services, other adapters)
- Your new adapter tests pass
- Application still works with in-memory adapter (default)

**Understanding Check:**
1. Did you need to change any domain code? Why not?
2. Did you need to change the port interface? Why not?
3. Can both `AuditSharedDB` and `AuditSystemAdapter` coexist? How?
4. What would happen if the external system changes its data model?

---

### Part 4: Switch Adapters (Optional)

**Goal:** See how easy it is to swap adapters.

**Update Spring configuration** to use your new adapter:

1. Find where `AuditSharedDB` is created (Spring `@Repository`)
2. Replace it with `AuditSystemAdapter`
3. Run the application
4. Verify comparisons are now stored via the Audit System

**Understanding Check:**
1. What files did you need to change to swap adapters?
2. Did the domain or services need to change?
3. What does this demonstrate about hexagonal architecture?

---

### ✅ Task 2 Complete When:

- [ ] `AuditSystemAdapter` implements `ComparisonHistoryRepository`
- [ ] Adapter correctly maps between domain and external system models
- [ ] All existing tests still pass
- [ ] You can explain why the adapter is needed
- [ ] You understand the role of mapping in adapters
- [ ] You can swap between `AuditSharedDB` and `AuditSystemAdapter`

**💡 Stuck?** A reference solution is available in `src/main/resources/task2-solution/` - but try to implement it yourself first!

---

### 💡 Key Learnings from Task 2:

1. **Requirements change, domain stays stable** - We added a new adapter without touching domain
2. **Adapters isolate external systems** - Domain doesn't know about `AuditSystem`
3. **Mapping is adapter responsibility** - Keep domain pure
4. **External systems have their own models** - We adapt to them, not vice versa
5. **Ports enable interchangeability** - Multiple adapters for same port
6. **Hexagonal architecture enables flexibility** - Easy to swap implementations
7. **Pattern reuse** - Similar adapters have similar structures

**The power of hexagonal architecture:** When requirements changed from shared database to external audit system API, we only needed to create a new adapter. The domain, services, and other adapters remained completely unchanged. This is the flexibility that hexagonal architecture provides!

**Congratulations!** You've now implemented a complete hexagonal architecture system with multiple adapters! 🎉

---

## 📖 Further Reading

- **Hexagonal Architecture** by Alistair Cockburn
- **Clean Architecture** by Robert C. Martin
- **Domain-Driven Design** by Eric Evans
- **Growing Object-Oriented Software, Guided by Tests** by Freeman & Pryce

---

**Ready to start? Begin with Task 1 to understand the complete implementation!** 🚀
