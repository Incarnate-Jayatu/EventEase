import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReportsPanel extends JPanel {
    private final EventManager events;
    private final RegistrationManager registrations;

    public ReportsPanel(EventManager events, RegistrationManager registrations) {
        this.events = events;
        this.registrations = registrations;
        JPanel page = UiTools.page("Reports & Export", "View registration totals and export event details.");
        setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JButton refresh = new JButton("Show Summary");
        JButton export = new JButton("Export Events to CSV");
        JTextArea summary = new JTextArea(7, 50);
        summary.setEditable(false);
        summary.setFont(new Font("Monospaced", Font.PLAIN, 13));

        refresh.addActionListener(e -> showSummary(summary));
        export.addActionListener(e -> exportCsv());

        content.add(refresh);
        content.add(Box.createVerticalStrut(12));
        content.add(new JScrollPane(summary));
        content.add(Box.createVerticalStrut(12));
        content.add(export);
        page.add(content, BorderLayout.CENTER);
        add(page);
        showSummary(summary);
    }

    private void showSummary(JTextArea area) {
        try {
            area.setText("Total events: " + events.getAllEvents().size()
                    + "\nConfirmed registrations: " + registrations.countByStatus("CONFIRMED")
                    + "\nWaitlisted registrations: " + registrations.countByStatus("WAITLISTED")
                    + "\nCancelled registrations: " + registrations.countByStatus("CANCELLED"));
        } catch (SQLException e) { UiTools.error(this, e); }
    }

    private void exportCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("eventease_events.csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {
            writer.write("ID,Name,Category,Date,Venue,Capacity\n");
            ArrayList<Event> list = events.getAllEvents();
            for (Event e : list) {
                writer.write(e.getId() + ",\"" + safe(e.getName()) + "\",\""
                        + safe(e.getCategory()) + "\"," + e.getDate() + ",\""
                        + safe(e.getVenue()) + "\"," + e.getCapacity() + "\n");
            }
            JOptionPane.showMessageDialog(this, "CSV file exported successfully.");
        } catch (Exception e) { UiTools.error(this, e); }
    }

    private String safe(String value) {
        return value == null ? "" : value.replace("\"", "\"\"");
    }
}
