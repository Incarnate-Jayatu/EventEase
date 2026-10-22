public class Registration {
    private int id;
    private String studentName;
    private String email;
    private String eventName;
    private String status;
    private String attendance;

    public Registration(int id, String studentName, String email, String eventName,
                        String status, String attendance) {
        this.id = id;
        this.studentName = studentName;
        this.email = email;
        this.eventName = eventName;
        this.status = status;
        this.attendance = attendance;
    }

    public int getId() { return id; }
    public String getStudentName() { return studentName; }
    public String getEmail() { return email; }
    public String getEventName() { return eventName; }
    public String getStatus() { return status; }
    public String getAttendance() { return attendance; }
}
