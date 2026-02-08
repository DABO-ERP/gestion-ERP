---
name: postgresql-persistence-design
description: Use this skill when modeling data, writing repositories, or configuring persistence.
---

When designing persistence:

1. Use:
   - PostgreSQL 15
   - Hibernate ORM 6.x
   - Spring Data JPA

2. Rules:
   - Entities represent aggregates
   - Repositories are per aggregate root
   - Transactions at use-case level

3. Performance:
   - Index foreign keys
   - Avoid EAGER fetching
   - No native queries unless justified

Data integrity > convenience.
Always prefer JPA and Hibernate features over raw SQL.
Don't use migration tools yet; write SQL migration scripts manually.