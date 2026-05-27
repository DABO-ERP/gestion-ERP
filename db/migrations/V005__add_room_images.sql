CREATE TABLE IF NOT EXISTS room_images (
    room_id VARCHAR(36) NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    image_url VARCHAR(1000) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_room_images_room_id ON room_images(room_id);

-- Replace picsum.photos placeholders with real hotel room images
UPDATE rooms SET room_type_image_url = 'https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=800&q=80' WHERE room_type_id = 'rt-001';
UPDATE rooms SET room_type_image_url = 'https://images.unsplash.com/photo-1590490360182-c33d57733427?w=800&q=80' WHERE room_type_id = 'rt-002';
UPDATE rooms SET room_type_image_url = 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=800&q=80' WHERE room_type_id = 'rt-003';
UPDATE rooms SET room_type_image_url = 'https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?w=800&q=80' WHERE room_type_id = 'rt-004';
UPDATE rooms SET room_type_image_url = 'https://images.unsplash.com/photo-1566665797739-1674de7a421a?w=800&q=80' WHERE room_type_id = 'rt-005';
