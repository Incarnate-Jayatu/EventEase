import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class RegistrationsPanel extends JPanel {
    private final EventManager events;
    private final RegistrationManager manager;
    private final DefaultTableModel model;

    public RegistrationsPanel(EventManager events, RegistrationManager manager) {
        this.events = events;
        this.manager = manager;
        JPanel page = UiTools.page("Registrations", "Register students and manage capacity-based waitlists.");
        setLayout(new BorderLayout());
        model = (DefaultTableModel) UiTools.table(
                new String[]{"ID", "Student", "Email", "Event", "Status", "Attendance"}).getModel();
        JTable table = new JTable(model);
        table.setRowHeight(28);

        JButton add = new JButton("Add Registration");
        JButton cancel = new JButton("Cancel Selected");
        add.addActionListener(e -> addRegistration());
        cancel.addActionListener(e -> cancelRegistration(table));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setOpaque(false);
        buttons.add(add); buttons.add(cancel);

        JPanel body = new JPanel(new BorderLayout(10, 12));
        body.setOpaque(false);
        body.add(new JScrollPane(table), BorderLayout.CENTER);
        body.add(buttons, BorderLayout.SOUTH);
        page.add(body, BorderLayout.CENTER);
        add(page);
        refresh();
    }

    private void refresh() {
        try {
            model.setRowCount(0);
            for (Registration r : manager.getAllRegistrations()) {
                model.addRow(new Object[]{r.getId(), r.getStudentName(), r.getEmail(),
                        r.getEventName(), r.getStatus(), r.getAttendance()});
            }
        } catch (SQLException e) { UiTools.error(this, e); }
    }

    private void addRegistration() {
        JTextField student = new JTextField();
        JTextField email = new JTextField();
        JComboBox<Event> eventBox = new JComboBox<>();
        try {
            ArrayList<Event> list = events.getAllEvents();
            for (Event event : list) eventBox.addItem(event);
        } catch (SQLException e) { UiTools.error(this, e); return; }

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Student name:")); form.add(student);
        form.add(new JLabel("Email:")); form.add(email);
        form.add(new JLabel("Event:")); form.add(eventBox);

        if (JOptionPane.showConfirmDialog(this, form, "Add Registration",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                if (student.getText().isBlank() || email.getText().isBlank())
                    throw new IllegalArgumentException("Student name and email are required.");
                Event selected = (Event) eventBox.getSelectedItem();
                if (selected == null) throw new IllegalArgumentException("Create an event first.");
                manager.addRegistration(student.getText().trim(), email.getText().trim(), selected.getId());
                JOptionPane.showMessageDialog(this, "Registration added. Check its status in the table.");
                refresh();
            } catch (Exception e) { UiTools.error(this, e); }
        }
    }

    private void cancelRegistration(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a registration first."); return; }
        int id = (int) model.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this,
                "Cancel this registration? If a seat opens, the first waitlisted student is promoted.",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try { manager.cancelRegistration(id); refresh(); }
            catch (SQLException e) { UiTools.error(this, e); }
        }
    }
}
