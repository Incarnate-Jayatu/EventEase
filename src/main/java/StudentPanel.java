import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentPanel extends JPanel {
    private final EventManager events;
    private final RegistrationManager registrations;
    private final DefaultTableModel model;
    private final JTable table;

    public StudentPanel(EventManager events, RegistrationManager registrations) {
        this.events = events;
        this.registrations = registrations;
        JPanel page = UiTools.page("Student Event Portal", "Browse campus events and register for an event.");
        setLayout(new BorderLayout());
        model = (DefaultTableModel) UiTools.table(
                new String[]{"ID", "Event", "Category", "Date", "Venue", "Capacity"}).getModel();
        table = new JTable(model);
        table.setRowHeight(30);

        JButton refresh = new JButton("Refresh Events");
        JButton register = new JButton("Register for Selected Event");
        refresh.addActionListener(e -> refreshEvents());
        register.addActionListener(e -> registerForEvent());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setOpaque(false);
        buttons.add(refresh);
        buttons.add(register);

        JPanel body = new JPanel(new BorderLayout(10, 12));
        body.setOpaque(false);
        body.add(new JScrollPane(table), BorderLayout.CENTER);
        body.add(buttons, BorderLayout.SOUTH);
        page.add(body, BorderLayout.CENTER);
        add(page);
        refreshEvents();
    }

    private void refreshEvents() {
        try {
            model.setRowCount(0);
            ArrayList<Event> list = events.getAllEvents();
            for (Event event : list) {
                model.addRow(new Object[]{event.getId(), event.getName(), event.getCategory(),
                        event.getDate(), event.getVenue(), event.getCapacity()});
            }
        } catch (SQLException e) { UiTools.error(this, e); }
    }

    private void registerForEvent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an event first.");
            return;
        }
        int eventId = (int) model.getValueAt(row, 0);
        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Student name:")); form.add(name);
        form.add(new JLabel("Email:")); form.add(email);

        if (JOptionPane.showConfirmDialog(this, form, "Register for Event",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (name.getText().isBlank() || email.getText().isBlank() || !email.getText().contains("@")) {
                JOptionPane.showMessageDialog(this, "Enter your name and a valid email address.");
                return;
            }
            try {
                registrations.addRegistration(name.getText().trim(), email.getText().trim(), eventId);
                JOptionPane.showMessageDialog(this,
                        "Registration submitted. Your status (Confirmed or Waitlisted) is available to the organizer.");
            } catch (SQLException e) {
                String message = e.getMessage() != null && e.getMessage().toLowerCase().contains("unique")
                        ? "This email is already registered for this event." : e.getMessage();
                JOptionPane.showMessageDialog(this, message, "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
