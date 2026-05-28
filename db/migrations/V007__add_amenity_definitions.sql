-- Create amenity definitions catalog
CREATE TABLE IF NOT EXISTS amenity_definitions (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Seed existing amenities from the enum
INSERT INTO amenity_definitions (id, name) VALUES
('ad-001', 'BATHROOM'),
('ad-002', 'TELEVISION'),
('ad-003', 'SOFA'),
('ad-004', 'BALCONY'),
('ad-005', 'AIR_CONDITIONING'),
('ad-006', 'WIFI'),
('ad-007', 'MINI_BAR'),
('ad-008', 'SAFE'),
('ad-009', 'DESK'),
('ad-010', 'WARDROBE')
ON CONFLICT (name) DO NOTHING;
