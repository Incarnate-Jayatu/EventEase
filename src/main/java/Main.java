import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Database.initialize();
                new LoginFrame().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Could not connect to the database.\n\n" + e.getMessage()
                        + "\n\nCheck MySQL and the database settings in Database.java.",
                        "Database Connection Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
