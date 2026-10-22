# EventEase System Flow

```text
Start -> Connect to MySQL -> Entry Screen
                            |
             +--------------+--------------+
             |                             |
       Admin Entry                    Student Entry
             |                             |
       Enter Password                Browse Events
             |                             |
       Password Valid?               Select Event
        /        \                       |
      No          Yes              Enter Name and Email
      |            |                       |
 Access Denied  Admin Dashboard     Check Duplicate / Capacity
                   |                       |
          +--------+--------+       Confirmed or Waitlisted
          |        |        |               |
       Events  Registrations Attendance/Reports
          |        |        |               |
          +--------+--------+---------------+
                            |
                      Return / Exit
```

Students can browse and register, while event creation, registration administration, attendance, and exports are available only from the password-protected admin entry.
