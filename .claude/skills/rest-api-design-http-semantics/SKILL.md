---
name: rest-api-design-http-semantics
description: Use this skill when designing, implementing, or reviewing REST endpoints and HTTP interactions between services or clients.
---

When designing REST APIs:

1. Follow REST conventions strictly:
   - GET: read-only
   - POST: create or commands
   - PUT: full update
   - PATCH: partial update
   - DELETE: deletion

2. Use correct HTTP status codes:
   - 200, 201, 204 for success
   - 400 for validation errors
   - 401 for unauthenticated
   - 403 for unauthorized
   - 404 for missing resources
   - 409 for conflicts

3. API structure:
   - `/api/v1/management`
   - Versioning is mandatory

4. Request/response:
   - JSON only
   - No envelope unless justified
   - Consistent error format

5. Avoid chatty APIs:
   - One request = one clear intent

Never expose internal domain models directly.