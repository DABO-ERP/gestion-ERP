-- V002: Seed Data for Development
-- Sample data for testing and development

-- Insert sample room types (embedded in rooms for this simplified version)
-- Insert sample rooms
INSERT INTO rooms (id, room_number, room_type_id, room_type_name, room_type_description, 
                   room_type_max_occupancy, room_type_base_price, room_status, created_at)
VALUES 
    ('room-001', 101, 'rt-001', 'Standard Single', 'Single room with basic amenities', 1, 50.00, 'AVAILABLE', CURRENT_DATE),
    ('room-002', 102, 'rt-002', 'Standard Double', 'Double room for two guests', 2, 80.00, 'AVAILABLE', CURRENT_DATE),
    ('room-003', 103, 'rt-003', 'Deluxe Suite', 'Luxury suite with premium amenities', 3, 150.00, 'AVAILABLE', CURRENT_DATE),
    ('room-004', 104, 'rt-004', 'Dormitory 4-Bed', 'Shared dormitory with 4 beds', 4, 25.00, 'AVAILABLE', CURRENT_DATE),
    ('room-005', 105, 'rt-004', 'Dormitory 4-Bed', 'Shared dormitory with 4 beds', 4, 25.00, 'AVAILABLE', CURRENT_DATE),
    ('room-006', 201, 'rt-002', 'Standard Double', 'Double room for two guests', 2, 80.00, 'AVAILABLE', CURRENT_DATE),
    ('room-007', 202, 'rt-003', 'Deluxe Suite', 'Luxury suite with premium amenities', 3, 150.00, 'OUT_OF_SERVICE', CURRENT_DATE),
    ('room-008', 203, 'rt-005', 'Family Room', 'Spacious room for families', 4, 120.00, 'AVAILABLE', CURRENT_DATE);

-- Insert amenities for rooms
INSERT INTO room_amenities (room_id, amenity) VALUES
    ('room-001', 'BATHROOM'),
    ('room-001', 'WIFI'),
    ('room-002', 'BATHROOM'),
    ('room-002', 'WIFI'),
    ('room-002', 'TELEVISION'),
    ('room-003', 'BATHROOM'),
    ('room-003', 'WIFI'),
    ('room-003', 'TELEVISION'),
    ('room-003', 'BALCONY'),
    ('room-003', 'AIR_CONDITIONING'),
    ('room-003', 'MINI_BAR'),
    ('room-004', 'WIFI'),
    ('room-005', 'WIFI'),
    ('room-006', 'BATHROOM'),
    ('room-006', 'WIFI'),
    ('room-006', 'TELEVISION'),
    ('room-007', 'BATHROOM'),
    ('room-007', 'WIFI'),
    ('room-007', 'TELEVISION'),
    ('room-007', 'BALCONY'),
    ('room-007', 'AIR_CONDITIONING'),
    ('room-008', 'BATHROOM'),
    ('room-008', 'WIFI'),
    ('room-008', 'TELEVISION'),
    ('room-008', 'AIR_CONDITIONING');

-- Insert beds for dormitory rooms
INSERT INTO beds (id, bed_number, room_id) VALUES
    ('bed-001', 1, 'room-004'),
    ('bed-002', 2, 'room-004'),
    ('bed-003', 3, 'room-004'),
    ('bed-004', 4, 'room-004'),
    ('bed-005', 1, 'room-005'),
    ('bed-006', 2, 'room-005'),
    ('bed-007', 3, 'room-005'),
    ('bed-008', 4, 'room-005');

-- Insert sample guests
INSERT INTO guests (id, first_name, last_name, email, phone, date_of_birth, 
                   nationality, document_number, document_type, created_at)
VALUES 
    ('guest-001', 'Juan', 'Pérez', 'juan.perez@example.com', '+57-300-1234567', '1990-05-15', 
     'COLOMBIA', 'CC1234567890', 'NATIONAL_ID', CURRENT_DATE),
    ('guest-002', 'María', 'García', 'maria.garcia@example.com', '+57-301-7654321', '1985-08-22', 
     'COLOMBIA', 'CC9876543210', 'NATIONAL_ID', CURRENT_DATE),
    ('guest-003', 'John', 'Smith', 'john.smith@example.com', '+1-555-0123', '1992-03-10', 
     'UNITED_STATES', 'PASS-US123456', 'PASSPORT', CURRENT_DATE),
    ('guest-004', 'Emma', 'Johnson', 'emma.johnson@example.com', '+1-555-0456', '1988-11-30', 
     'UNITED_STATES', 'PASS-US789012', 'PASSPORT', CURRENT_DATE),
    ('guest-005', 'Carlos', 'Rodríguez', 'carlos.rodriguez@example.com', '+34-600-123456', '1995-07-18', 
     'SPAIN', 'DNI12345678', 'IDENTITY_CARD', CURRENT_DATE);

-- Insert sample reservations
INSERT INTO reservations (id, reservation_code, check_in, check_out, status_id, status_type, 
                         status_note, quoted_amount, source, created_at, guest_principal_id, 
                         room_id, stay_id, stay_check_in, stay_check_out)
VALUES 
    ('res-001', 'RES-A1B2C3D4', CURRENT_DATE + INTERVAL '2 days', CURRENT_DATE + INTERVAL '5 days', 
     'status-001', 'CONFIRMED', 'Reservation confirmed via email', 240.00, 'DIRECT', CURRENT_DATE, 
     'guest-001', 'room-002', NULL, NULL, NULL),
    ('res-002', 'RES-E5F6G7H8', CURRENT_DATE + INTERVAL '1 day', CURRENT_DATE + INTERVAL '4 days', 
     'status-002', 'CONFIRMED', 'Booking from Hostelworld', 75.00, 'HOSTELWORLD', CURRENT_DATE, 
     'guest-003', 'room-004', NULL, NULL, NULL),
    ('res-003', 'RES-I9J0K1L2', CURRENT_DATE - INTERVAL '2 days', CURRENT_DATE + INTERVAL '1 day', 
     'status-003', 'CHECKED_IN', 'Guest checked in', 450.00, 'BOOKING', CURRENT_DATE - INTERVAL '2 days', 
     'guest-005', 'room-003', 'stay-001', CURRENT_DATE - INTERVAL '2 days', NULL);

-- Link additional guests to reservations
INSERT INTO reservation_guests (reservation_id, guest_id) VALUES
    ('res-001', 'guest-001'),
    ('res-001', 'guest-002'),
    ('res-002', 'guest-003'),
    ('res-003', 'guest-005');

-- Add a note to a guest
UPDATE guests 
SET notes_id = 'note-001', 
    notes_text = 'VIP guest - prefers upper floors', 
    notes_level = 'NORMAL'
WHERE id = 'guest-005';
