package newpack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.FileWriter;


public class EventEase extends JFrame implements ActionListener {                  //CLASS EVENTEASE
    Connection con;
    JLabel message;
    JButton adminButton, studentButton, exitButton;

    public EventEase() {
        setTitle("EventEase - Campus Event Management System");
        setSize(420, 260);
        setLayout(new GridLayout(5, 1, 5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new JLabel("EventEase Campus Event Management", JLabel.CENTER));
        add(new JLabel("Choose a portal", JLabel.CENTER));

        adminButton = new JButton("Admin Login");
        studentButton = new JButton("Student Portal");
        exitButton = new JButton("Exit");
        adminButton.addActionListener(this);
        studentButton.addActionListener(this);
        exitButton.addActionListener(this);
        add(adminButton);
        add(studentButton);
        add(exitButton);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == adminButton) {
            new LoginWindow();
            dispose();
        } else if (e.getSource() == studentButton) {
            new StudentWindow();
            dispose();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

   
    public static Connection connect() {
    	    Connection con = null;

    	    try {
    	    	Class.forName("com.mysql.jdbc.Driver");
    	        con = DriverManager.getConnection(
    	            "jdbc:mysql://localhost:3306/eventease_db",
    	            "root",
    	            "root"
    	        );

    	        System.out.println("Database connected successfully");

    	    } catch (Exception e) {
    	        System.out.println("Database connection failed!");
    	        System.out.println(e.getMessage());
    	        e.printStackTrace();
    	    }

    	    return con;
    	}

    public static void main(String[] args) {
        new EventEase();
    }
}


class LoginWindow extends JFrame implements ActionListener {                           //CLASS LOGINWINDOW
    JTextField passwordField;
    JLabel message;
    JButton loginButton, backButton;

    LoginWindow() {
        setTitle("EventEase - Admin Login"); setSize(350, 180);
        setLayout(new GridLayout(3, 2, 5, 5)); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setLocationRelativeTo(null);
        add(new JLabel("Admin Password:")); passwordField = new JTextField(); add(passwordField);
        loginButton = new JButton("Login"); backButton = new JButton("Back");
        add(loginButton); add(backButton); message = new JLabel("Enter admin123"); add(message); add(new JLabel(""));
        loginButton.addActionListener(this); backButton.addActionListener(this); setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            if (passwordField.getText().equals("admin123")) { new AdminWindow(); dispose(); }
            else message.setText("Incorrect password.");
        } else if (e.getSource() == backButton) { new EventEase(); dispose(); }
    }
}

class AdminWindow extends JFrame implements ActionListener {                             //CLASS ADMINWINDOW
    Connection con;
    JTextField idField, nameField, descriptionField, dateField, venueField, capacityField, registrationIdField;
    JLabel message;
    JButton addButton, updateButton, deleteButton, viewEventsButton, viewRegistrationsButton;
    JButton presentButton, absentButton, reportButton, exportButton, switchButton;

    AdminWindow() {
        setTitle("EventEase - Admin");
        setSize(650, 650);
        setLayout(new GridLayout(0, 2, 5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new JLabel("Event ID (for update/delete):")); idField = new JTextField(); add(idField);
        add(new JLabel("Event Name:")); nameField = new JTextField(); add(nameField);
        add(new JLabel("Description:")); descriptionField = new JTextField(); add(descriptionField);
        add(new JLabel("Date (YYYY-MM-DD):")); dateField = new JTextField(); add(dateField);
        add(new JLabel("Venue:")); venueField = new JTextField(); add(venueField);
        add(new JLabel("Capacity:")); capacityField = new JTextField(); add(capacityField);

        addButton = new JButton("Add Event"); updateButton = new JButton("Update Event");
        deleteButton = new JButton("Delete Event"); viewEventsButton = new JButton("View Events");
        viewRegistrationsButton = new JButton("View Registrations"); reportButton = new JButton("View Report");
        add(addButton); add(updateButton); add(deleteButton); add(viewEventsButton);
        add(viewRegistrationsButton); add(reportButton);

        add(new JLabel("Registration ID (for attendance):")); registrationIdField = new JTextField(); add(registrationIdField);
        presentButton = new JButton("Mark Present"); absentButton = new JButton("Mark Absent");
        add(presentButton); add(absentButton);
        exportButton = new JButton("Export CSV"); switchButton = new JButton("Switch Profile");
        add(exportButton); add(switchButton);
        message = new JLabel("Ready", JLabel.CENTER); add(message); add(new JLabel(""));

        addButton.addActionListener(this); updateButton.addActionListener(this); deleteButton.addActionListener(this);
        viewEventsButton.addActionListener(this); viewRegistrationsButton.addActionListener(this);
        presentButton.addActionListener(this); absentButton.addActionListener(this); reportButton.addActionListener(this);
        exportButton.addActionListener(this); switchButton.addActionListener(this);

        try { con = EventEase.connect(); message.setText("Database connected"); }
        catch (Exception ex) { message.setText("DB error: " + ex.getMessage()); }
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == switchButton) {
                if (con != null) con.close();
                new EventEase(); dispose(); return;
            }
            if (con == null) { message.setText("Database not connected. Check MySQL/driver/password."); return; }
            if (e.getSource() == addButton) addEvent();
            else if (e.getSource() == updateButton) updateEvent();
            else if (e.getSource() == deleteButton) deleteEvent();
            else if (e.getSource() == viewEventsButton) viewEvents();
            else if (e.getSource() == viewRegistrationsButton) viewRegistrations();
            else if (e.getSource() == presentButton) markAttendance("PRESENT");
            else if (e.getSource() == absentButton) markAttendance("ABSENT");
            else if (e.getSource() == reportButton) viewReport();
            else if (e.getSource() == exportButton) exportCSV();
        } catch (Exception ex) { message.setText("Error: " + ex.getMessage()); }
    }

    void addEvent() throws Exception {
        if (nameField.getText().trim().isEmpty() || dateField.getText().trim().isEmpty() || venueField.getText().trim().isEmpty() || capacityField.getText().trim().isEmpty()) {
            message.setText("Enter event name, date, venue and capacity."); return;
        }
        String sql = "INSERT INTO events(event_name, description, event_date, venue, capacity) VALUES(?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nameField.getText().trim());
        ps.setString(2, descriptionField.getText().trim());
        ps.setDate(3, Date.valueOf(dateField.getText().trim()));
        ps.setString(4, venueField.getText().trim());
        ps.setInt(5, Integer.parseInt(capacityField.getText().trim()));
        ps.executeUpdate(); message.setText("Event added successfully."); clearFields();
    }

    void updateEvent() throws Exception {
        String sql = "UPDATE events SET event_name=?, description=?, event_date=?, venue=?, capacity=? WHERE event_id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nameField.getText().trim()); ps.setString(2, descriptionField.getText().trim());
        ps.setDate(3, Date.valueOf(dateField.getText().trim())); ps.setString(4, venueField.getText().trim());
        ps.setInt(5, Integer.parseInt(capacityField.getText().trim())); ps.setInt(6, Integer.parseInt(idField.getText().trim()));
        int rows = ps.executeUpdate(); message.setText(rows > 0 ? "Event updated." : "Event ID not found.");
    }

    void deleteEvent() throws Exception {
        int id = Integer.parseInt(idField.getText().trim());
        PreparedStatement rp = con.prepareStatement("DELETE FROM registrations WHERE event_id=?");
        rp.setInt(1, id); rp.executeUpdate();
        PreparedStatement ep = con.prepareStatement("DELETE FROM events WHERE event_id=?");
        ep.setInt(1, id); int rows = ep.executeUpdate();
        message.setText(rows > 0 ? "Event and registrations deleted." : "Event ID not found.");
    }

    void viewEvents() throws Exception {
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery("SELECT event_id,event_name,description,event_date,venue,capacity FROM events ORDER BY event_date");
        String result = "EVENT LIST\n\n";
        while (rs.next()) {
            result += "ID: " + rs.getInt("event_id") + " | Name: " + rs.getString("event_name") +
                    " | Description: " + rs.getString("description") + " | Date: " + rs.getDate("event_date") +
                    " | Venue: " + rs.getString("venue") + " | Capacity: " + rs.getInt("capacity") + "\n";
        }
        new ResultWindow("Events", result.isEmpty() ? "No events found." : result);
    }

    void viewRegistrations() throws Exception {
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery("SELECT registration_id,event_id,student_name,email,status,attendance FROM registrations ORDER BY registration_id DESC");
        String result = "REGISTRATIONS\n\n";
        while (rs.next()) result += "Registration ID: " + rs.getInt("registration_id") + " | Event ID: " + rs.getInt("event_id") +
                " | Student: " + rs.getString("student_name") + " | Email: " + rs.getString("email") +
                " | Status: " + rs.getString("status") + " | Attendance: " + rs.getString("attendance") + "\n";
        new ResultWindow("Registrations", result);
    }

    void markAttendance(String value) throws Exception {
        PreparedStatement ps = con.prepareStatement("UPDATE registrations SET attendance=? WHERE registration_id=? AND status='CONFIRMED'");
        ps.setString(1, value); ps.setInt(2, Integer.parseInt(registrationIdField.getText().trim()));
        int rows = ps.executeUpdate(); message.setText(rows > 0 ? "Attendance marked " + value + "." : "Confirmed registration not found.");
    }

    void viewReport() throws Exception {
        Statement s = con.createStatement();
        ResultSet r1 = s.executeQuery("SELECT COUNT(*) FROM events"); r1.next(); int events = r1.getInt(1);
        ResultSet r2 = s.executeQuery("SELECT COUNT(*) FROM registrations WHERE status='CONFIRMED'"); r2.next(); int confirmed = r2.getInt(1);
        ResultSet r3 = s.executeQuery("SELECT COUNT(*) FROM registrations WHERE status='WAITLISTED'"); r3.next(); int waiting = r3.getInt(1);
        new ResultWindow("EventEase Report", "EVENTEASE REPORT\n\nTotal events: " + events + "\nConfirmed registrations: " + confirmed + "\nWaitlisted registrations: " + waiting);
    }

    void exportCSV() throws Exception {
        FileWriter fw = new FileWriter("EventEase_Registrations.csv");
        fw.write("Registration ID,Event ID,Student Name,Email,Status,Attendance\n");
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery("SELECT registration_id,event_id,student_name,email,status,attendance FROM registrations");
        while (rs.next()) {
            fw.write(rs.getInt(1) + "," + rs.getInt(2) + ",\"" + rs.getString(3) + "\",\"" + rs.getString(4) + "\"," + rs.getString(5) + "," + rs.getString(6) + "\n");
        }
        fw.close(); message.setText("CSV saved as EventEase_Registrations.csv in project folder.");
    }

    void clearFields() {
        nameField.setText(""); descriptionField.setText(""); dateField.setText(""); venueField.setText(""); capacityField.setText("");
    }
}

class StudentWindow extends JFrame implements ActionListener {              //CLASS SWTUDENTWINDOW
    Connection con;
    JTextField eventIdField, nameField, emailField;
    JLabel message;
    JButton viewButton, registerButton, switchButton;

    StudentWindow() {
        setTitle("EventEase - Student Portal"); setSize(500, 300);
        setLayout(new GridLayout(5, 2, 5, 5)); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setLocationRelativeTo(null);
        add(new JLabel("Event ID:")); eventIdField = new JTextField(); add(eventIdField);
        add(new JLabel("Student Name:")); nameField = new JTextField(); add(nameField);
        add(new JLabel("Student Email:")); emailField = new JTextField(); add(emailField);
        viewButton = new JButton("View Events"); registerButton = new JButton("Register");
        add(viewButton); add(registerButton);
        switchButton = new JButton("Switch Profile"); message = new JLabel("Ready"); add(switchButton); add(message);
        viewButton.addActionListener(this); registerButton.addActionListener(this); switchButton.addActionListener(this);
        try { con = EventEase.connect(); message.setText("Database connected"); }
        catch (Exception ex) { message.setText("DB error: " + ex.getMessage()); }
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == switchButton) { if (con != null) con.close(); new EventEase(); dispose(); return; }
            if (con == null) { message.setText("Database not connected."); return; }
            if (e.getSource() == viewButton) {
                Statement s = con.createStatement(); ResultSet rs = s.executeQuery("SELECT event_id,event_name,event_date,venue,capacity FROM events ORDER BY event_date");
                String result = "AVAILABLE EVENTS\n\n";
                while (rs.next()) result += "ID: " + rs.getInt(1) + " | " + rs.getString(2) + " | " + rs.getDate(3) + " | " + rs.getString(4) + " | Capacity: " + rs.getInt(5) + "\n";
                new ResultWindow("Available Events", result);
            } else if (e.getSource() == registerButton) registerStudent();
        } catch (Exception ex) { message.setText("Error: " + ex.getMessage()); }
    }

    void registerStudent() throws Exception {
        if (eventIdField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
            message.setText("Enter event ID, name and email."); return;
        }
        int eventId = Integer.parseInt(eventIdField.getText().trim());
        PreparedStatement eventCheck = con.prepareStatement("SELECT capacity FROM events WHERE event_id=?");
        eventCheck.setInt(1, eventId); ResultSet er = eventCheck.executeQuery();
        if (!er.next()) { message.setText("Event ID not found."); return; }
        PreparedStatement check = con.prepareStatement("SELECT registration_id FROM registrations WHERE event_id=? AND email=? AND status<>'CANCELLED'");
        check.setInt(1, eventId); check.setString(2, emailField.getText().trim()); ResultSet cr = check.executeQuery();
        if (cr.next()) { message.setText("This email is already registered."); return; }
        PreparedStatement count = con.prepareStatement("SELECT COUNT(*) FROM registrations WHERE event_id=? AND status='CONFIRMED'");
        count.setInt(1, eventId); ResultSet countResult = count.executeQuery(); countResult.next();
        String status = countResult.getInt(1) >= er.getInt("capacity") ? "WAITLISTED" : "CONFIRMED";
        PreparedStatement insert = con.prepareStatement("INSERT INTO registrations(event_id,student_name,email,status,attendance) VALUES(?,?,?,?,?)");
        insert.setInt(1, eventId); insert.setString(2, nameField.getText().trim()); insert.setString(3, emailField.getText().trim());
        insert.setString(4, status); insert.setString(5, "NOT MARKED"); insert.executeUpdate();
        message.setText("Registration saved: " + status);
    }
}

class ResultWindow extends JFrame {                           //CLASS RESULTWINDOW
    ResultWindow(String title, String text) {
        setTitle(title); setSize(700, 450); setLayout(new FlowLayout()); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); setLocationRelativeTo(null);
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) add(new JLabel(lines[i]));
        setVisible(true);
    }
}