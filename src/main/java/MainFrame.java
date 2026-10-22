import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel pages = new JPanel(cardLayout);
    private final EventManager eventManager = new EventManager();
    private final RegistrationManager registrationManager = new RegistrationManager();

    public MainFrame(String role) {
        boolean admin = "ADMIN".equals(role);
        setTitle("EventEase - " + (admin ? "Admin Workspace" : "Student Portal"));
        setSize(1150, 720);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(215, 0));
        sidebar.setBackground(new Color(25, 38, 67));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(22, 14, 20, 14));

        JLabel brand = new JLabel("EventEase");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("SansSerif", Font.BOLD, 24));
        sidebar.add(brand);
        JLabel roleLabel = new JLabel(admin ? "ADMIN WORKSPACE" : "STUDENT PORTAL");
        roleLabel.setForeground(new Color(185, 198, 220));
        roleLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(roleLabel);
        sidebar.add(Box.createVerticalStrut(30));

        if (admin) {
            addNav(sidebar, "Dashboard", "Dashboard");
            addNav(sidebar, "Manage Events", "Events");
            addNav(sidebar, "Registrations", "Registrations");
            addNav(sidebar, "Attendance", "Attendance");
            addNav(sidebar, "Reports & Export", "Reports");
            pages.add(new DashboardPanel(eventManager, registrationManager), "Dashboard");
            pages.add(new EventsPanel(eventManager), "Events");
            pages.add(new RegistrationsPanel(eventManager, registrationManager), "Registrations");
            pages.add(new AttendancePanel(registrationManager), "Attendance");
            pages.add(new ReportsPanel(eventManager, registrationManager), "Reports");
            cardLayout.show(pages, "Dashboard");
        } else {
            addNav(sidebar, "Browse Events", "Student");
            pages.add(new StudentPanel(eventManager, registrationManager), "Student");
            cardLayout.show(pages, "Student");
        }

        sidebar.add(Box.createVerticalGlue());
        JButton logout = new JButton("Back to Entry Screen");
        logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        logout.setMaximumSize(new Dimension(185, 40));
        logout.setForeground(Color.WHITE);
        logout.setBackground(new Color(45, 62, 95));
        logout.setFocusPainted(false);
        logout.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });
        sidebar.add(logout);

        add(sidebar, BorderLayout.WEST);
        add(pages, BorderLayout.CENTER);
    }

    private void addNav(JPanel sidebar, String label, String page) {
        JButton button = new JButton(label);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(185, 42));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(25, 38, 67));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 5));
        button.addActionListener(e -> cardLayout.show(pages, page));
        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(8));
    }
}
