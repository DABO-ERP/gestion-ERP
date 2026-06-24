CREATE TABLE payments (
    id VARCHAR(36) PRIMARY KEY,
    reservation_id VARCHAR(36) NOT NULL REFERENCES reservations(id),
    amount DECIMAL(10,2) NOT NULL,
    method VARCHAR(50) NOT NULL,
    paid_at TIMESTAMP NOT NULL,
    note TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_payments_reservation_id ON payments(reservation_id);
