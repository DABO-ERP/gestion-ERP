-- V008: Create room_blocks table for date-range out-of-service periods
CREATE TABLE room_blocks (
    id VARCHAR(36) PRIMARY KEY,
    room_id VARCHAR(36) NOT NULL REFERENCES rooms(id),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_dates CHECK (end_date > start_date)
);

CREATE INDEX idx_room_blocks_room_id ON room_blocks(room_id);
CREATE INDEX idx_room_blocks_dates ON room_blocks(start_date, end_date);
