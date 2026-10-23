# EventEase — Campus Event Management System

EventEase is a beginner-friendly Java desktop application for managing college events and student registrations. It uses Java Swing for the graphical interface and MySQL for storing event and registration information.

## Features

### Admin
- Password-protected admin login.
- Add, update, delete, and view events.
- View student registration records.
- Mark confirmed registrations as **PRESENT** or **ABSENT**.
- View a summary report containing total events, confirmed registrations, and waitlisted registrations.
- Export registration data to a CSV file.
- Switch back to the main menu.

### Student
- View available events.
- Register using an event ID, student name, and email address.
- Prevent duplicate active registrations for the same event and email.
- Assign `CONFIRMED` when seats are available or `WAITLISTED` when capacity is reached.

## Technology Stack
- **Language:** Java
- **GUI:** Java Swing (`JFrame`, `JLabel`, `JTextField`, `JButton`)
- **Layouts:** `GridLayout` and `FlowLayout`
- **Database:** MySQL
- **Connectivity:** JDBC with MySQL Connector/J
- **CSV export:** Java `FileWriter`
- **IDE:** Eclipse

## Project Structure

The current implementation is contained in one Java source file:

```text
ManageEvent/
└── src/
    └── newpack/
        └── EventEase.java
```

| Class | Responsibility |
|---|---|
| `EventEase` | Main menu and database connection method |
| `LoginWindow` | Admin password verification |
| `AdminWindow` | Event management, registrations, attendance, reports, and CSV export |
| `StudentWindow` | Event browsing and student registration |
| `ResultWindow` | Displays event lists, registration lists, and report output |

## Prerequisites
1. A compatible Java Development Kit (JDK).
2. Eclipse IDE or another Java IDE.
3. MySQL Server.
4. MySQL Connector/J JAR.
5. The `eventease_db` database with the required tables.

## Database Setup

The application currently connects to:
- **Host:** `localhost`
- **Port:** `3306`
- **Database:** `eventease_db`
- **Username:** `root`
- **Password:** `root` (used in the current code; change it if your local MySQL password differs).

Create the database if it does not exist:

```sql
CREATE DATABASE IF NOT EXISTS eventease_db;
USE eventease_db;
```

Expected table structure:

```sql
CREATE TABLE IF NOT EXISTS events (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    event_name VARCHAR(100) NOT NULL,
    description TEXT,
    event_date DATE NOT NULL,
    venue VARCHAR(100),
    capacity INT NOT NULL
);

CREATE TABLE IF NOT EXISTS registrations (
    registration_id INT PRIMARY KEY AUTO_INCREMENT,
    event_id INT NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    attendance VARCHAR(20) DEFAULT 'ABSENT',
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(event_id)
);
```

> If the database and tables already exist, inspect them using `DESCRIBE events;` and `DESCRIBE registrations;`. Do not drop or recreate tables that contain data. The SQL above documents the expected structure.

## Configure MySQL Connector/J

The working setup uses:

```java
Class.forName("com.mysql.jdbc.Driver");
```

This is compatible with the MySQL Connector/J **5.1.49** JAR used during setup.

In Eclipse:
1. Right-click the project.
2. Select **Build Path → Configure Build Path**.
3. Open **Libraries** and click **Add External JARs**.
4. Select the MySQL Connector/J JAR.
5. Click **Apply and Close**.

With newer Connector/J versions, the driver class may instead be `com.mysql.cj.jdbc.Driver`.

## Run in Eclipse
1. Open the project in Eclipse.
2. Confirm `EventEase.java` is inside package `newpack`.
3. Start MySQL Server.
4. Confirm Connector/J is on the project Build Path.
5. Verify the database credentials in `EventEase.connect()`.
6. Run `EventEase.java` as a **Java Application**.

## How to Use

### Admin
1. Select **Admin Login** from the main menu.
2. Enter the current admin password: `admin123`.
3. Use the fields and buttons to add, update, delete, and view events.
4. To update or delete an event, enter its event ID.
5. To mark attendance, enter the registration ID and choose **Mark Present** or **Mark Absent**.
6. Select **View Report** for summary counts or **Export CSV** to export registration records.

### Student
1. Select **Student Portal** from the main menu.
2. Click **View Events** to view available events.
3. Enter the event ID, student name, and email.
4. Click **Register**.
5. The result shows `CONFIRMED` or `WAITLISTED`.

## CSV Export

The **Export CSV** button creates `EventEase_Registrations.csv` in the application's current working directory. The file contains registration ID, event ID, student name, email, status, and attendance.

## Current Limitations
- The admin password (`admin123`) is hard-coded in the source.
- The password uses a regular `JTextField`, so it is visible while typing.
- The application is configured for a local MySQL server.
- Results are displayed using labels in a separate window rather than a table.
- Registration cancellation and automatic waitlist promotion are **not implemented**.
- CSV export uses simple formatting and may need extra escaping for commas or quotation marks.
- Database credentials are stored in source code for this academic project.

## Possible Future Improvements
- Secure password storage and configurable admin credentials.
- Improved validation for dates, email addresses, and capacity.
- Registration cancellation and automatic waitlist promotion.
- Improved CSV escaping and a file-save location selector.
- Event search and filtering.
- More detailed event-wise and attendance reports.
- A more advanced interface as additional Swing components are learned.

## Troubleshooting

### `ClassNotFoundException: com.mysql.jdbc.Driver`
Check that Connector/J is added to the Eclipse Build Path and that the driver class matches the JAR version.

### `Access denied for user 'root'`
Ensure the password in `EventEase.connect()` matches the local MySQL account.

### `Communications link failure`
Confirm MySQL Server is running and listening on port `3306`.

### `Unknown database 'eventease_db'`
Confirm the database exists on the server named in the connection URL.

If the connection fails, read the full exception in the Eclipse Console and troubleshoot that specific message instead of recreating the database immediately.

## Security Note
This is an academic project. Before using it with real student information, move credentials out of the source code, secure the admin login, validate inputs, and review database permissions.

