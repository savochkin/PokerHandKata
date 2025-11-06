---
trigger: always_on
description: 
globs: 
---

# Rule: Hexagonal architecture for solution design

**Description:**  
All new solutions must be designed and reasoned about using Hexagonal (Ports & Adapters) architecture. Before any code is written, we will discuss the use case, its impact on the domain model, and which external systems are involved. Infrastructure concerns must be isolated from the domain.

## Activation
- Applies to: **all files and tasks** (design, docs, code)
- Behavior: **Discuss first, code only after explicit approval** (e.g., “Let’s start coding”)

## Design discussion checklist (must be addressed before coding)
1. **Use case**: What user/business goal triggers this? Happy path + key alternatives.
2. **Domain model**: New/changed aggregates, entities, value objects, invariants.
3. **Ports**:
   - **Inbound ports** (application service interfaces) that expose use cases.
   - **Outbound ports** (domain-required interfaces) for external dependencies.
4. **Adapters**:
   - **In adapters** (`adapters/in/...`): UI, API (including BFF), CLI, message listeners, schedulers.
   - **Out adapters** (`adapters/out/...`): DB, caches, external HTTP/gRPC, third-party APIs.
   - External API specifics are **hidden inside out adapters**; domain sees only ports.
5. **Boundaries & data**:
   - No external types leak into domain; map to domain DTOs/Value Objects.
   - Transactions, retries, caching, serialization stay **outside** the domain.
6. **Impacted external systems**: List systems, protocols, SLAs, failure modes.
7. **Testing strategy**: Domain tests (pure), port contract tests, adapter integration tests.

## Do / Don’t
- **Do** keep domain pure: business rules, invariants, policies without I/O.
- **Do** define clear **ports** first; implement adapters later.
- **Do** keep BFF/API in `adapters/in` and hide provider quirks in `adapters/out`.
- **Don’t** reference frameworks, ORMs, HTTP clients, or vendor SDKs from domain.
- **Don’t** pass infrastructure models into domain; always map to domain objects.
- In adapters should only communicate with the app via in ports, no direct service calling allowed.
- Domain objects should only communicate with external systems via out ports, no direct calls of adapters allowed.

## Reference folder layout (example)
/app/domain - domain model (no spring allowed) containing domain objects
/app/service - domain services (can be spring components)
/app/port/in - inbound ports (use-case interfaces)
/app/port/out - outbound ports (required interfaces)
/adapters/in - inbound adapters (driving adapters), e.g. UI, REST/GraphQL, BFF, consumers
/adapters/out - outbound adapters (driven adapters), e.g. DB, HTTP clients, queues, providers
/config - wiring, DI, framework bootstrap

## Acceptance questions (quick gate before implementation)
- What are the inbound + outbound ports?
- What changed in the domain model and why?
- Which external systems are touched and how are they isolated behind ports?
- Where do mappings occur to avoid leaking infra types into domain?
- How will we test domain, ports, and adapters separately?

## Tests
- domain tests should be behavioural, i.e. test should be per use case (not per class).
- a domain test should either use mocks for out ports or test stub.
- start with implementation of tests for domain first.