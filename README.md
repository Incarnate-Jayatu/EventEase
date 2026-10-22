# EventEase — Campus Event Manager

A beginner-friendly Java Swing desktop application for campus event management. It uses MySQL through JDBC.

## Entry options
- **Admin:** password-protected entry for dashboard, event management, registration viewing/cancellation, attendance, and reports/CSV export.
- **Student:** public entry to browse events and register using name and email. Students cannot access admin controls.
- Demo admin password: `admin123` (change `ADMIN_PASSWORD` in `LoginFrame.java` before presenting). This is a classroom prototype; a real deployment should use secure password storage and server-side authentication.

## Features
- Dashboard with event, confirmed registration, and waitlist counts
- Event CRUD: add, view, edit, delete
- Student registration with duplicate event/email protection
- Capacity-based automatic waitlisting
- When a confirmed registration is cancelled, the earliest waitlisted student is promoted
- Attendance marking (Present/Absent)
- Registration summary and CSV export of events

## Requirements
- JDK 17+
- Maven
- MySQL 8+

## 1. Create the database
1. Start MySQL.
2. Open MySQL Workbench or another SQL client.
3. Run `database/schema.sql`.
4. Optional: run `database/sample_data.sql`.

## 2. Configure the database connection
Open `src/main/java/Database.java` and change `USER` and `PASSWORD` if needed. Admin password is set separately in `src/main/java/LoginFrame.java`.

Defaults:
- URL: `jdbc:mysql://localhost:3306/eventease_db?serverTimezone=UTC`
- User: `root`
- Password: `password`

You can override the defaults with environment variables:
- `EVENTEASE_DB_URL`
- `EVENTEASE_DB_USER`
- `EVENTEASE_DB_PASSWORD`

## 3. Run
Open a terminal in the project folder:

```bash
mvn clean compile
mvn exec:java
```

Or import the folder as a Maven project in IntelliJ IDEA/Eclipse and run `Main.java`.

## Notes
- All main operations use JDBC prepared statements.
- Data is stored in MySQL, not just in memory.
- Deleting an event also deletes its registrations because of the foreign key cascade.
- The CSV export currently exports the event list.
- This project is designed for learning. Test with sample data before your demonstration.
