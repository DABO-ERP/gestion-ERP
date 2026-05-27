-- V004: Add room_type_image_url column and seed dummy images

ALTER TABLE rooms ADD COLUMN IF NOT EXISTS room_type_image_url VARCHAR(500);

UPDATE rooms SET room_type_image_url = 'https://picsum.photos/seed/standard-single/400/300' WHERE room_type_id = 'rt-001';
UPDATE rooms SET room_type_image_url = 'https://picsum.photos/seed/standard-double/400/300' WHERE room_type_id = 'rt-002';
UPDATE rooms SET room_type_image_url = 'https://picsum.photos/seed/deluxe-suite/400/300' WHERE room_type_id = 'rt-003';
UPDATE rooms SET room_type_image_url = 'https://picsum.photos/seed/dormitory/400/300' WHERE room_type_id = 'rt-004';
UPDATE rooms SET room_type_image_url = 'https://picsum.photos/seed/family-room/400/300' WHERE room_type_id = 'rt-005';
