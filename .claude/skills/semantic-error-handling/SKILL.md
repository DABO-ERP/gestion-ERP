---
name: semantic-error-handling
description: Use this skill when defining error responses, exception handling, or failure scenarios.
---

When handling errors:

1. Use `@ControllerAdvice`
2. Error response must include:
   - errorCode (semantic, stable)
   - message (human-readable)
   - timestamp
   - path

3. Rules:
   - Never leak stack traces
   - Never expose internal exceptions

Errors are part of the API contract.
Always map exceptions to meaningful HTTP status codes.