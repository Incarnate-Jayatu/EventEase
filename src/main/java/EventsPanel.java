import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;

public class EventsPanel extends JPanel {
    private final EventManager manager;
    private final DefaultTableModel model;

    public EventsPanel(EventManager manager) {
        this.manager = manager;
        JPanel page = UiTools.page("Manage Events", "Add, edit, delete, and view campus events.");
        setLayout(new BorderLayout());
        model = (DefaultTableModel) UiTools.table(
                new String[]{"ID", "Name", "Category", "Date", "Venue", "Capacity"}).getModel();
        JTable table = new JTable(model);
        table.setRowHeight(28);

        JButton add = new JButton("Add Event");
        JButton edit = new JButton("Edit Selected");
        JButton delete = new JButton("Delete Selected");
        add.addActionListener(e -> addEvent());
        edit.addActionListener(e -> editEvent(table));
        delete.addActionListener(e -> deleteEvent(table));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setOpaque(false);
        buttons.add(add); buttons.add(edit); buttons.add(delete);

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
            for (Event e : manager.getAllEvents()) {
                model.addRow(new Object[]{e.getId(), e.getName(), e.getCategory(),
                        e.getDate(), e.getVenue(), e.getCapacity()});
            }
        } catch (SQLException e) { UiTools.error(this, e); }
    }

    private void addEvent() {
        JTextField name = new JTextField();
        JTextField category = new JTextField();
        JTextField date = new JTextField(LocalDate.now().plusDays(7).toString());
        JTextField venue = new JTextField();
        JTextField capacity = new JTextField("50");
        JPanel form = form(name, category, date, venue, capacity);
        if (JOptionPane.showConfirmDialog(this, form, "Add Event",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                if (name.getText().isBlank()) throw new IllegalArgumentException("Event name is required.");
                int seats = Integer.parseInt(capacity.getText().trim());
                if (seats < 1) throw new IllegalArgumentException("Capacity must be positive.");
                manager.addEvent(name.getText().trim(), category.getText().trim(),
                        LocalDate.parse(date.getText().trim()), venue.getText().trim(), seats);
                refresh();
            } catch (Exception e) { UiTools.error(this, e); }
        }
    }

    private JPanel form(JTextField name, JTextField category, JTextField date,
                        JTextField venue, JTextField capacity) {
        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.add(new JLabel("Event name:")); p.add(name);
        p.add(new JLabel("Category:")); p.add(category);
        p.add(new JLabel("Date (YYYY-MM-DD):")); p.add(date);
        p.add(new JLabel("Venue:")); p.add(venue);
        p.add(new JLabel("Capacity:")); p.add(capacity);
        return p;
    }

    private void editEvent(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select an event first."); return; }
        int id = (int) model.getValueAt(row, 0);
        JTextField name = new JTextField(model.getValueAt(row, 1).toString());
        JTextField category = new JTextField(model.getValueAt(row, 2).toString());
        JTextField date = new JTextField(model.getValueAt(row, 3).toString());
        JTextField venue = new JTextField(model.getValueAt(row, 4).toString());
        JTextField capacity = new JTextField(model.getValueAt(row, 5).toString());
        if (JOptionPane.showConfirmDialog(this, form(name, category, date, venue, capacity),
                "Edit Event", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                manager.updateEvent(id, name.getText().trim(), category.getText().trim(),
                        LocalDate.parse(date.getText().trim()), venue.getText().trim(),
                        Integer.parseInt(capacity.getText().trim()));
                refresh();
            } catch (Exception e) { UiTools.error(this, e); }
        }
    }

    private void deleteEvent(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select an event first."); return; }
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this event and its registrations?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try { manager.deleteEvent(id); refresh(); }
            catch (SQLException e) { UiTools.error(this, e); }
        }
    }
}
