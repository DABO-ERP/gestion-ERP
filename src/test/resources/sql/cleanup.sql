-- Cleanup script for acceptance tests
-- This script cleans the database before each test scenario

-- Clean tables in correct order to handle foreign key constraints
DELETE FROM reservation_guests;
DELETE FROM reservations;
DELETE FROM rooms;
DELETE FROM room_types;
DELETE FROM guests;

-- Reset sequences if needed (for PostgreSQL)
-- ALTER SEQUENCE guest_id_seq RESTART WITH 1;
-- ALTER SEQUENCE room_type_id_seq RESTART WITH 1;
-- ALTER SEQUENCE room_id_seq RESTART WITH 1;
-- ALTER SEQUENCE reservation_id_seq RESTART WITH 1;