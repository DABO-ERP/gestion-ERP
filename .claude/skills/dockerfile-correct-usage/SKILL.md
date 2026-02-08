---
name: dockerfile-correct-usage
description: Use this skill when building Docker images for Spring Boot microservices.
---

When writing Dockerfiles:

1. Use multi-stage builds
2. Base image:
   - eclipse-temurin:17-jre-jammy

3. Rules:
   - Non-root user
   - Expose port explicitly
   - Healthcheck defined

4. Image tagging:
   - Semantic versioning (MAJOR.MINOR.PATCH)

5. Configuration:
   - Use environment variables
   - No secrets in image

Docker images must be reproducible and minimal.
Always follow best practices for security and efficiency.