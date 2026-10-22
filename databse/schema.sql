CREATE DATABASE IF NOT EXISTS eventease_db;
USE eventease_db;

CREATE TABLE IF NOT EXISTS events (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL,
    category VARCHAR(60),
    event_date DATE NOT NULL,
    venue VARCHAR(120),
    capacity INT NOT NULL,
    CHECK (capacity > 0)
);

CREATE TABLE IF NOT EXISTS registrations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL,
    event_id INT NOT NULL,
    status ENUM('CONFIRMED', 'WAITLISTED', 'CANCELLED') NOT NULL DEFAULT 'CONFIRMED',
    attendance ENUM('NOT MARKED', 'PRESENT', 'ABSENT') NOT NULL DEFAULT 'NOT MARKED',
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_registration_event FOREIGN KEY (event_id)
        REFERENCES events(id) ON DELETE CASCADE,
    CONSTRAINT unique_event_email UNIQUE (event_id, email)
);
