---
name: auth-agnostic-architecture-boundary
description: Use this skill to enforce strict separation between core system architecture and authentication/authorization concerns.
---

## Architectural Boundary Rule

This project is **completely agnostic to authentication and authorization**.

If a request reaches the system:
- The user is already authenticated
- The user is already authorized
- No permission checks are required

---

## Core Principle

**Authentication and authorization are external concerns.**

They are handled by:
- API gateways
- Identity providers
- BFFs
- External security layers

They are **never** handled inside:
- Domain logic
- Application use cases
- Core services
- Business rules

---

## Explicit Rules

- Do not model roles, permissions, or access levels
- Do not check user privileges in code
- Do not branch logic based on user identity or role
- Do not leak auth concepts into domain language

The system assumes **trusted, pre-authorized input**.

---

## Mental Model for the AI Agent

Think of the system as operating in a **secure internal network**:
- Every request is valid
- Every action is permitted
- Focus exclusively on business correctness

If a feature requires authorization logic,  
**it is out of scope by design**.

Violations of this rule are **architectural bugs**, not security improvements.