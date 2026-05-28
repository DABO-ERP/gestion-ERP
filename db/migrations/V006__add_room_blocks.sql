CREATE TABLE IF NOT EXISTS room_blocks (
    id VARCHAR(36) PRIMARY KEY,
    room_id VARCHAR(36) NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE INDEX IF NOT EXISTS idx_room_blocks_room_id ON room_blocks(room_id);
CREATE INDEX IF NOT EXISTS idx_room_blocks_dates ON room_blocks(room_id, start_date, end_date);

-- Seed 3-5 real images per room (picsum.photos deterministic seeds)
INSERT INTO room_images (room_id, image_url) VALUES
('room-001', 'https://picsum.photos/seed/r101a/800/600'),
('room-001', 'https://picsum.photos/seed/r101b/800/600'),
('room-001', 'https://picsum.photos/seed/r101c/800/600'),
('room-002', 'https://picsum.photos/seed/r102a/800/600'),
('room-002', 'https://picsum.photos/seed/r102b/800/600'),
('room-002', 'https://picsum.photos/seed/r102c/800/600'),
('room-002', 'https://picsum.photos/seed/r102d/800/600'),
('room-003', 'https://picsum.photos/seed/r103a/800/600'),
('room-003', 'https://picsum.photos/seed/r103b/800/600'),
('room-003', 'https://picsum.photos/seed/r103c/800/600'),
('room-003', 'https://picsum.photos/seed/r103d/800/600'),
('room-003', 'https://picsum.photos/seed/r103e/800/600'),
('room-004', 'https://picsum.photos/seed/r104a/800/600'),
('room-004', 'https://picsum.photos/seed/r104b/800/600'),
('room-004', 'https://picsum.photos/seed/r104c/800/600'),
('room-005', 'https://picsum.photos/seed/r105a/800/600'),
('room-005', 'https://picsum.photos/seed/r105b/800/600'),
('room-005', 'https://picsum.photos/seed/r105c/800/600'),
('room-006', 'https://picsum.photos/seed/r201a/800/600'),
('room-006', 'https://picsum.photos/seed/r201b/800/600'),
('room-006', 'https://picsum.photos/seed/r201c/800/600'),
('room-006', 'https://picsum.photos/seed/r201d/800/600'),
('room-007', 'https://picsum.photos/seed/r202a/800/600'),
('room-007', 'https://picsum.photos/seed/r202b/800/600'),
('room-007', 'https://picsum.photos/seed/r202c/800/600'),
('room-008', 'https://picsum.photos/seed/r203a/800/600'),
('room-008', 'https://picsum.photos/seed/r203b/800/600'),
('room-008', 'https://picsum.photos/seed/r203c/800/600'),
('room-008', 'https://picsum.photos/seed/r203d/800/600');
