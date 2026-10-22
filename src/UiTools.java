import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UiTools {
    public static JPanel page(String title, String subtitle) {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBackground(new Color(245, 247, 251));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        JLabel h = new JLabel(title);
        h.setFont(new Font("SansSerif", Font.BOLD, 25));
        JLabel s = new JLabel(subtitle);
        s.setForeground(new Color(105, 115, 132));
        s.setBorder(new EmptyBorder(6, 0, 0, 0));
        heading.add(h);
        heading.add(s);
        panel.add(heading, BorderLayout.NORTH);
        return panel;
    }

    public static JTable table(String[] columns) {
        JTable table = new JTable(new javax.swing.table.DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        });
        table.setRowHeight(28);
        return table;
    }

    public static void error(Component parent, Exception e) {
        JOptionPane.showMessageDialog(parent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
