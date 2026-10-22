USE eventease_db;

INSERT INTO events (name, category, event_date, venue, capacity)
SELECT 'TechFest 2026', 'Technology', '2026-11-20', 'Main Auditorium', 150
WHERE NOT EXISTS (SELECT 1 FROM events WHERE name = 'TechFest 2026');

INSERT INTO events (name, category, event_date, venue, capacity)
SELECT 'Design Sprint', 'Design', '2026-11-24', 'Lab 3', 60
WHERE NOT EXISTS (SELECT 1 FROM events WHERE name = 'Design Sprint');

INSERT INTO events (name, category, event_date, venue, capacity)
SELECT 'Coding Meetup', 'Workshop', '2026-11-29', 'Seminar Hall', 3
WHERE NOT EXISTS (SELECT 1 FROM events WHERE name = 'Coding Meetup');
