import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class AttendancePanel extends JPanel {
    private final RegistrationManager manager;
    private final DefaultTableModel model;

    public AttendancePanel(RegistrationManager manager) {
        this.manager = manager;
        JPanel page = UiTools.page("Attendance", "Mark attendance for confirmed registrations.");
        setLayout(new BorderLayout());
        model = (DefaultTableModel) UiTools.table(
                new String[]{"ID", "Student", "Email", "Event", "Attendance"}).getModel();
        JTable table = new JTable(model);
        table.setRowHeight(28);

        JButton present = new JButton("Mark Present");
        JButton absent = new JButton("Mark Absent");
        present.addActionListener(e -> mark(table, "PRESENT"));
        absent.addActionListener(e -> mark(table, "ABSENT"));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setOpaque(false);
        buttons.add(present); buttons.add(absent);

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
                if (r.getStatus().equals("CONFIRMED")) {
                    model.addRow(new Object[]{r.getId(), r.getStudentName(), r.getEmail(),
                            r.getEventName(), r.getAttendance()});
                }
            }
        } catch (SQLException e) { UiTools.error(this, e); }
    }

    private void mark(JTable table, String status) {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a student first."); return; }
        int id = (int) model.getValueAt(row, 0);
        try {
            manager.markAttendance(id, status);
            refresh();
        } catch (SQLException e) { UiTools.error(this, e); }
    }
}
