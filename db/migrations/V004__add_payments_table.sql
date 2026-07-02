-- V003: Add payments table for reservation payments

CREATE TABLE IF NOT EXISTS payments (
    id VARCHAR(36) PRIMARY KEY,
    reservation_id VARCHAR(36) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    method VARCHAR(20) NOT NULL,
    note TEXT,
    paid_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    voided BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE,
    CONSTRAINT chk_amount_positive CHECK (amount > 0)
);

CREATE INDEX idx_payments_reservation ON payments(reservation_id);
CREATE INDEX idx_payments_voided ON payments(voided);

COMMENT ON TABLE payments IS 'Stores payments made for reservations';
