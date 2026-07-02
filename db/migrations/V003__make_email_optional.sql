-- V003: Make email optional for guests
-- Allows NULL email while keeping UNIQUE constraint (PostgreSQL allows multiple NULLs)

ALTER TABLE guests ALTER COLUMN email DROP NOT NULL;
