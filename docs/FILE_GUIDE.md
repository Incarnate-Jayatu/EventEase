File Guide
`Main.java`: checks database connection and opens the entry screen.
`LoginFrame.java`: provides Admin and Student entry options; asks for a password for Admin.
`MainFrame.java`: displays role-specific navigation and prevents student users from seeing admin pages.
`StudentPanel.java`: lets students browse events and register.
`Database.java`: JDBC connection settings.
`Event.java`: class representing one event.
`Registration.java`: class representing one registration.
`EventManager.java`: event database operations (add, view, edit, delete).
`RegistrationManager.java`: registration creation, capacity checks, waitlist promotion, cancellation, attendance.
`DashboardPanel.java`: admin dashboard totals.
`EventsPanel.java`: admin event management.
`RegistrationsPanel.java`: admin registration list and cancellation.
`AttendancePanel.java`: admin attendance screen.
`ReportsPanel.java`: admin summary and CSV export.
`UiTools.java`: shared UI helpers.
Suggested learning order
`Event.java` → `Registration.java` → `Database.java` → manager classes → `LoginFrame.java` → `MainFrame.java` → `StudentPanel.java` → admin screens.
