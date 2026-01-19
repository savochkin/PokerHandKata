# Hexagonal Architecture Kata - Invoice Pricing

A 30–40 minute kata in Java to experience the core idea of hexagonal architecture (Ports & Adapters) using an invoice pricing service as an example. 

You start from a simple, coupled implementation (in com.example.invoice.v1) and compare it with a hexagonal version (in com.example.invoice.hex) where the core depends on a port interface, and an adapter implements that port.


## Problem: Invoice Pricing Service

We have a simple invoice pricing service:
- An Invoice has multiple LineItems. 
- Each line item has quantity and unitPrice. 
- The service should:
  - Compute subtotal = sum of quantity * unitPrice. 
  - Compute tax = 20% of subtotal. 
  - Compute total = subtotal + tax. 
  - Store the priced invoice in an in‑memory repository.

We implement this in two styles:
1. v1 – a simple, coupled version. 
2. hex – using hexagonal architecture.

## 🎓 Learning Objectives

By completing ths kata, you will:
- ✅ Understand the core idea of hexagonal architecture (Ports & Adapters)
- ✅ Identify the port (InvoiceRepository) and adapter (InMemoryInvoiceRepository) in hexagonal architecture.
- ✅ See how dependency inversion makes the core easier to test.
- ✅ Write a test for InvoiceService that uses a fake repository instead of the real adapter.


## Step 1: Inspect the v1 Implementation

**Look at:**
- com.example.invoice.v1.InvoiceService
- com.example.invoice.v1.InMemoryInvoiceRepository
- com.example.invoice.v1.Main

**What to notice:**
- InvoiceService:
  - Contains pricing logic (subtotal, tax, total). 
  - Constructs InMemoryInvoiceRepository directly using new. 
- Persistence and business rules are tightly coupled.

There is no way to inject a different repository implementation.

### Discusssion Points 
- If you wanted to store invoices in a file instead of memory, what would you change? 
- How hard would it be to unit‑test pricing logic without touching the real repository?
- Does the service depend on abstractions, or on concrete classes?


## Step 2 – Inspect the Hexagonal Implementation

**2.1 Domain**
- These are plain Java classes, no repository or I/O references. 
- Only business data and behavior.

**2.2 Outbound Port**
The core declares what it needs (a way to save invoices) as an interface.

**2.3 Application Service**
- Depends on InvoiceRepository (the port).
- Implements the pricing use case: 
  - Compute subtotal, tax, total. 
  - Save via repository.save(pricedInvoice). 
  - Return the priced invoice.

The service does not know which concrete repository implementation it is using.

**2.4 Outbound Adapter**
- Implements InvoiceRepository. 
- Stores invoices in an in‑memory list. 
- Has no knowledge of how pricing works.

This is the adapter that sits on the outside and satisfies the port.

**2.5 Composition Root** 
- Creates InMemoryInvoiceRepository.
- Creates InvoiceService, injecting the repository. 
- Builds a sample Invoice with LineItems. 
- Calls service.price(invoice) and prints the result.

This is where we "plug" the adapter into the core.

## Step 3 – Testing the Core with a Fake Repository
Test InvoiceService without any real adapter. 

Takeaways
- No need to spin up databases or reuse adapters in unit tests.
- You can assert that InvoiceService both:
- computes correct totals, and 
- calls save on the repository.

### Architecture Overview

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
                    ║    │    │        ║
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

## Key discussion points

- Explain in 1–2 sentences the difference in dependency direction between v1 and hex.
- Point to the port and the adapter in the hex code.
- Show in the hex tests how the service is tested without the real adapter.
