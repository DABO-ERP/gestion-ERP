---
name: professional-unit-testing
description: Use this skill when writing or reviewing automated tests.
---

When writing tests:

1. Use:
   - JUnit 5
   - Mockito 5.x
   - AssertJ

2. Test types:
   - Unit tests for domain & use cases
   - Slice tests for controllers & repositories

3. Rules:
   - No Spring context in unit tests
   - No testing implementation details
   - Tests must be deterministic

Untested auth logic is unacceptable.
Always follow the Arrange-Act-Assert pattern in tests.