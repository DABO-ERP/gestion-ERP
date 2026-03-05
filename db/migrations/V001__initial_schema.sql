-- V001: Initial Schema Creation
-- Gestion ERP - Hostel Management System

-- Create guests table
CREATE TABLE IF NOT EXISTS guests (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    date_of_birth DATE,
    nationality VARCHAR(50) NOT NULL,
    document_number VARCHAR(50) NOT NULL,
    document_type VARCHAR(50) NOT NULL,
    notes_id VARCHAR(36),
    notes_text TEXT,
    notes_level VARCHAR(20),
    created_at DATE NOT NULL,
    CONSTRAINT chk_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

CREATE INDEX idx_guests_email ON guests(email);
CREATE INDEX idx_guests_document ON guests(document_number);
CREATE INDEX idx_guests_created_at ON guests(created_at);

-- Create rooms table
CREATE TABLE IF NOT EXISTS rooms (
    id VARCHAR(36) PRIMARY KEY,
    room_number INTEGER NOT NULL UNIQUE,
    room_type_id VARCHAR(36) NOT NULL,
    room_type_name VARCHAR(100) NOT NULL,
    room_type_description TEXT,
    room_type_max_occupancy INTEGER NOT NULL,
    room_type_base_price DECIMAL(10, 2) NOT NULL,
    room_status VARCHAR(50) NOT NULL,
    created_at DATE NOT NULL,
    CONSTRAINT chk_room_number_positive CHECK (room_number > 0),
    CONSTRAINT chk_max_occupancy_positive CHECK (room_type_max_occupancy > 0),
    CONSTRAINT chk_base_price_positive CHECK (room_type_base_price > 0)
);

CREATE INDEX idx_rooms_number ON rooms(room_number);
CREATE INDEX idx_rooms_status ON rooms(room_status);
CREATE INDEX idx_rooms_type_id ON rooms(room_type_id);

-- Create room_amenities table
CREATE TABLE IF NOT EXISTS room_amenities (
    room_id VARCHAR(36) NOT NULL,
    amenity VARCHAR(50) NOT NULL,
    PRIMARY KEY (room_id, amenity),
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE
);

-- Create beds table
CREATE TABLE IF NOT EXISTS beds (
    id VARCHAR(36) PRIMARY KEY,
    bed_number INTEGER NOT NULL,
    room_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    CONSTRAINT chk_bed_number_positive CHECK (bed_number > 0),
    CONSTRAINT uq_bed_room UNIQUE (room_id, bed_number)
);

CREATE INDEX idx_beds_room_id ON beds(room_id);

-- Create reservations table
CREATE TABLE IF NOT EXISTS reservations (
    id VARCHAR(36) PRIMARY KEY,
    reservation_code VARCHAR(50) NOT NULL UNIQUE,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    status_id VARCHAR(36),
    status_type VARCHAR(50) NOT NULL,
    status_note TEXT,
    quoted_amount DECIMAL(10, 2) NOT NULL,
    source VARCHAR(50) NOT NULL,
    created_at DATE NOT NULL,
    guest_principal_id VARCHAR(36) NOT NULL,
    room_id VARCHAR(36) NOT NULL,
    stay_id VARCHAR(36),
    stay_check_in DATE,
    stay_check_out DATE,
    FOREIGN KEY (guest_principal_id) REFERENCES guests(id),
    FOREIGN KEY (room_id) REFERENCES rooms(id),
    CONSTRAINT chk_checkout_after_checkin CHECK (check_out > check_in),
    CONSTRAINT chk_quoted_amount_non_negative CHECK (quoted_amount >= 0)
);

CREATE INDEX idx_reservations_code ON reservations(reservation_code);
CREATE INDEX idx_reservations_guest ON reservations(guest_principal_id);
CREATE INDEX idx_reservations_room ON reservations(room_id);
CREATE INDEX idx_reservations_dates ON reservations(check_in, check_out);
CREATE INDEX idx_reservations_status ON reservations(status_type);
CREATE INDEX idx_reservations_created_at ON reservations(created_at);

-- Create reservation_guests table (many-to-many)
CREATE TABLE IF NOT EXISTS reservation_guests (
    reservation_id VARCHAR(36) NOT NULL,
    guest_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (reservation_id, guest_id),
    FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE,
    FOREIGN KEY (guest_id) REFERENCES guests(id)
);

CREATE INDEX idx_reservation_guests_reservation ON reservation_guests(reservation_id);
CREATE INDEX idx_reservation_guests_guest ON reservation_guests(guest_id);

-- Add comments for documentation
COMMENT ON TABLE guests IS 'Stores guest information for the hostel management system';
COMMENT ON TABLE rooms IS 'Physical rooms in the hostel with their type and status';
COMMENT ON TABLE reservations IS 'Bookings made by guests';
COMMENT ON TABLE beds IS 'Individual beds within rooms (important for hostels)';
