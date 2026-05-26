-- V003: Add deleted column for soft delete support on rooms

ALTER TABLE rooms ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT FALSE;

CREATE INDEX idx_rooms_deleted ON rooms(deleted);
