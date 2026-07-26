package view;

import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class StudentTablePanel extends JPanel {
    private static final String[] COLUMNS = {
            "MSSV", "Họ tên", "Lớp", "Ngày sinh", "GPA", "Trạng thái", "Email"
    };

    private final DefaultTableModel tableModel;
    private final JTable table;

    public StudentTablePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 35));

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        styleTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(new Color(18, 18, 35));
        scroll.setBackground(new Color(18, 18, 35));

        add(scroll, BorderLayout.CENTER);
    }

    private void styleTable() {
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(new Color(220, 220, 235));
        table.setBackground(new Color(25, 25, 45));
        table.setGridColor(new Color(50, 50, 75));
        table.setSelectionBackground(new Color(99, 102, 241));
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(15, 15, 30));
        table.getTableHeader().setForeground(new Color(165, 180, 252));
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? new Color(25, 25, 45) : new Color(30, 30, 55));
                    c.setForeground(new Color(210, 210, 230));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        };
        centerRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        DefaultTableCellRenderer gpaRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                if (!sel && val != null) {
                    try {
                        double gpa = Double.parseDouble(val.toString());
                        if (gpa >= 3.6) c.setForeground(new Color(74, 222, 128));
                        else if (gpa >= 3.0) c.setForeground(new Color(250, 204, 21));
                        else if (gpa >= 2.0) c.setForeground(new Color(251, 146, 60));
                        else c.setForeground(new Color(248, 113, 113));
                        c.setBackground(row % 2 == 0 ? new Color(25, 25, 45) : new Color(30, 30, 55));
                    } catch (NumberFormatException ignored) {}
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };

        for (int i = 0; i < COLUMNS.length; i++) {
            if (i == 4) table.getColumnModel().getColumn(i).setCellRenderer(gpaRenderer);
            else table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(180);
    }

    public void populate(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                    s.getMssv(),
                    s.getHoTen(),
                    s.getLop(),
                    s.getNgaySinh(),
                    String.format("%.2f", s.getGpa()),
                    s.getTrangThai().getLabel(),
                    s.getEmail()
            });
        }
    }

    public JTable getTable() { return table; }

    public String getSelectedMssv() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        return (String) tableModel.getValueAt(row, 0);
    }
}
