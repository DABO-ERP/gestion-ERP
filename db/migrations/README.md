# Database Migrations

This directory contains SQL migration scripts for the Gestion ERP database schema.

## Naming Convention

Migrations follow the Flyway naming convention:
- `V{version}__{description}.sql` for versioned migrations
- Example: `V001__initial_schema.sql`

## Execution Order

Migrations are executed in version order. Each migration should be:
- Idempotent when possible
- Tested before deployment
- Never modified after being applied to production

## How to Add a New Migration

1. Create a new file with the next version number
2. Write SQL DDL/DML statements
3. Test locally
4. Deploy

## Current Migrations

- V001: Initial schema creation (guests, rooms, reservations)
- V002: Seed data for development
