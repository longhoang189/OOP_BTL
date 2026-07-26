package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class StatisticsPanel extends JPanel {
    private static final Color BG      = new Color(18, 18, 35);
    private static final Color SURFACE = new Color(25, 25, 48);
    private static final Color ACCENT  = new Color(165, 180, 252);

    private final DefaultTableModel tableModel;
    private final JLabel lblTotal  = statLabel("0");
    private final JLabel lblAvgGpa = statLabel("0.00");
    private final JLabel lblTopGpa = statLabel("0.00");

    public StatisticsPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 24, 20, 24));

        add(buildSummaryCard(), BorderLayout.NORTH);

        String[] cols = {"Lớp", "Số sinh viên", "GPA Trung bình", "GPA Thấp nhất", "GPA Cao nhất"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = buildTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 80), 1));
        scroll.getViewport().setBackground(SURFACE);

        JLabel tableTitle = new JLabel("Thống kê theo lớp");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(ACCENT);
        tableTitle.setBorder(new EmptyBorder(0, 0, 8, 0));

        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setBackground(BG);
        tableWrap.add(tableTitle, BorderLayout.NORTH);
        tableWrap.add(scroll, BorderLayout.CENTER);
        add(tableWrap, BorderLayout.CENTER);
    }

    private JPanel buildSummaryCard() {
        JPanel card = new JPanel(new GridLayout(1, 3, 16, 0));
        card.setBackground(BG);
        card.add(buildCard("Tổng sinh viên", lblTotal,  new Color(99, 102, 241)));
        card.add(buildCard("GPA trung bình", lblAvgGpa, new Color(16, 185, 129)));
        card.add(buildCard("GPA cao nhất",   lblTopGpa, new Color(245, 158, 11)));
        return card;
    }

    private JPanel buildCard(String title, JLabel valLabel, Color accent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(SURFACE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accent.darker(), 1),
                new EmptyBorder(16, 20, 16, 20)));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(148, 163, 184));

        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valLabel.setForeground(accent);

        p.add(lbl, BorderLayout.NORTH);
        p.add(valLabel, BorderLayout.CENTER);
        return p;
    }

    private JLabel statLabel(String text) {
        JLabel l = new JLabel(text);
        l.setHorizontalAlignment(SwingConstants.LEFT);
        return l;
    }

    private JTable buildTable() {
        JTable table = new JTable(tableModel);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setBackground(SURFACE);
        table.setForeground(new Color(210, 210, 235));
        table.setGridColor(new Color(45, 45, 70));
        table.setSelectionBackground(new Color(99, 102, 241));
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(15, 15, 30));
        table.getTableHeader().setForeground(ACCENT);

        DefaultTableCellRenderer cr = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? SURFACE : new Color(30, 30, 55));
                }
                setBorder(new EmptyBorder(0, 12, 0, 12));
                setHorizontalAlignment(col == 0 ? LEFT : CENTER);
                return c;
            }
        };
        for (int i = 0; i < 5; i++) table.getColumnModel().getColumn(i).setCellRenderer(cr);
        return table;
    }

    public void update(Map<String, double[]> stats, int total) {
        lblTotal.setText(String.valueOf(total));
        double sumAvg = 0, maxGpa = 0;
        tableModel.setRowCount(0);
        for (Map.Entry<String, double[]> e : stats.entrySet()) {
            double[] d = e.getValue();
            tableModel.addRow(new Object[]{
                    e.getKey(),
                    (int) d[0],
                    String.format("%.2f", d[1]),
                    String.format("%.2f", d[2]),
                    String.format("%.2f", d[3])
            });
            sumAvg += d[1];
            if (d[3] > maxGpa) maxGpa = d[3];
        }
        if (!stats.isEmpty()) {
            lblAvgGpa.setText(String.format("%.2f", sumAvg / stats.size()));
        } else {
            lblAvgGpa.setText("0.00");
        }
        lblTopGpa.setText(String.format("%.2f", maxGpa));
    }
}
