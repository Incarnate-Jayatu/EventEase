import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    // Demo password for the college prototype. Change this before presenting.
    private static final String ADMIN_PASSWORD = "admin123";

    public LoginFrame() {
        setTitle("EventEase - Choose Entry Type");
        setSize(760, 470);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(10, 20));
        root.setBackground(new Color(245, 247, 251));
        root.setBorder(new EmptyBorder(30, 35, 30, 35));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Welcome to EventEase");
        title.setFont(new Font("SansSerif", Font.BOLD, 29));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle = new JLabel("Campus Event Management System");
        subtitle.setForeground(new Color(100, 110, 130));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        heading.add(title);
        heading.add(Box.createVerticalStrut(8));
        heading.add(subtitle);
        root.add(heading, BorderLayout.NORTH);

        JPanel choices = new JPanel(new GridLayout(1, 2, 18, 18));
        choices.setOpaque(false);
        choices.add(createChoice("ADMIN", "Manage events, registrations, attendance and reports.", "Enter as Admin", true));
        choices.add(createChoice("STUDENT", "Browse upcoming events and register to participate.", "Enter as Student", false));
        root.add(choices, BorderLayout.CENTER);

        JLabel note = new JLabel("Admin access is password protected.", SwingConstants.CENTER);
        note.setForeground(new Color(100, 110, 130));
        root.add(note, BorderLayout.SOUTH);
        add(root);
    }

    private JPanel createChoice(String heading, String description, String buttonText, boolean admin) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 228, 238)),
                new EmptyBorder(22, 20, 22, 20)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(heading);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setForeground(new Color(35, 52, 85));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextArea details = new JTextArea(description);
        details.setWrapStyleWord(true);
        details.setLineWrap(true);
        details.setEditable(false);
        details.setOpaque(false);
        details.setFont(new Font("SansSerif", Font.PLAIN, 13));
        details.setAlignmentX(Component.CENTER_ALIGNMENT);
        details.setMaximumSize(new Dimension(260, 70));

        JButton button = new JButton(buttonText);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(65, 105, 225));
        button.setBorder(new EmptyBorder(11, 15, 11, 15));
        button.addActionListener(e -> {
            if (admin) enterAdmin();
            else openWorkspace("STUDENT");
        });

        card.add(label);
        card.add(Box.createVerticalStrut(16));
        card.add(details);
        card.add(Box.createVerticalGlue());
        card.add(button);
        return card;
    }

    private void enterAdmin() {
        JPasswordField password = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(this, password, "Admin Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;
        if (ADMIN_PASSWORD.equals(new String(password.getPassword()))) {
            openWorkspace("ADMIN");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect admin password.",
                    "Access Denied", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openWorkspace(String role) {
        new MainFrame(role).setVisible(true);
        dispose();
    }
}
