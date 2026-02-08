---
name: professional-gradle-usage
description: Use this skill when configuring builds, dependencies, testing, or artifact generation using Gradle.
---

When using Gradle:

1. Use **Gradle 8.6**
2. Dependency rules:
   - No dynamic versions
   - Explicit versions only
   - Use dependency locking

3. Build outputs:
   - Executable JAR
   - Docker-ready artifact

4. Testing:
   - Separate unit and integration tests
   - Fail build on test failure

Gradle scripts must be readable and deterministic.
Always leverage Gradle's built-in tasks and plugins before creating custom ones.