import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class DashboardPanel extends JPanel {
    private final EventManager events;
    private final RegistrationManager registrations;
    private final JLabel eventCount = new JLabel("-");
    private final JLabel confirmedCount = new JLabel("-");
    private final JLabel waitlistCount = new JLabel("-");

    public DashboardPanel(EventManager events, RegistrationManager registrations) {
        this.events = events;
        this.registrations = registrations;
        JPanel page = UiTools.page("Dashboard", "Overview of campus events and student registrations.");
        JPanel cards = new JPanel(new GridLayout(1, 3, 15, 15));
        cards.setOpaque(false);
        cards.add(card("Total Events", eventCount));
        cards.add(card("Confirmed Registrations", confirmedCount));
        cards.add(card("Waitlisted Students", waitlistCount));
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(cards, BorderLayout.NORTH);
        JLabel note = new JLabel("Use the left navigation to manage events, registrations, attendance, and reports.");
        note.setBorder(BorderFactory.createEmptyBorder(25, 3, 0, 0));
        center.add(note, BorderLayout.CENTER);
        page.add(center, BorderLayout.CENTER);
        setLayout(new BorderLayout());
        add(page);
        refresh();
    }

    private JPanel card(String title, JLabel value) {
        JPanel p = new JPanel(new GridLayout(2, 1, 5, 5));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        value.setFont(new Font("SansSerif", Font.BOLD, 30));
        value.setForeground(new Color(65, 105, 225));
        p.add(new JLabel(title));
        p.add(value);
        return p;
    }

    private void refresh() {
        try {
            eventCount.setText(String.valueOf(events.getAllEvents().size()));
            confirmedCount.setText(String.valueOf(registrations.countByStatus("CONFIRMED")));
            waitlistCount.setText(String.valueOf(registrations.countByStatus("WAITLISTED")));
        } catch (SQLException e) {
            UiTools.error(this, e);
        }
    }
}
