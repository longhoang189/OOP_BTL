package view;

import model.StudentStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class SearchPanel extends JPanel {
    private final JTextField txtSearch  = new JTextField();
    private final JComboBox<String> cbxStatus;
    private final JComboBox<String> cbxSort;
    private final JButton btnSearch = new JButton("Tìm kiếm");
    private final JButton btnReset  = new JButton("Tất cả");

    private static final Color BG = new Color(15, 15, 30);
    private static final Color SURFACE = new Color(22, 22, 42);
    private static final Color TEXT = new Color(210, 210, 235);
    private static final Color ACCENT = new Color(99, 102, 241);

    public SearchPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        setBackground(BG);
        setBorder(new EmptyBorder(6, 12, 6, 12));

        String[] statusOpts = {"Tất cả trạng thái", "Đang học", "Tốt nghiệp", "Bảo lưu", "Đình chỉ"};
        cbxStatus = new JComboBox<>(statusOpts);

        String[] sortOpts = {"Mặc định", "Tên A→Z", "Tên Z→A", "GPA cao→thấp", "GPA thấp→cao"};
        cbxSort = new JComboBox<>(sortOpts);

        JLabel lSearch = label("Tìm kiếm:");
        JLabel lStatus = label("Lọc:");
        JLabel lSort   = label("Sắp xếp:");

        txtSearch.setPreferredSize(new Dimension(220, 32));
        styleField(txtSearch);
        styleCombo(cbxStatus);
        styleCombo(cbxSort);
        styleBtn(btnSearch, ACCENT);
        styleBtn(btnReset, new Color(55, 65, 81));

        add(lSearch);
        add(txtSearch);
        add(lStatus);
        add(cbxStatus);
        add(lSort);
        add(cbxSort);
        add(btnSearch);
        add(btnReset);
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(new Color(148, 163, 184));
        return l;
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBackground(SURFACE);
        f.setForeground(TEXT);
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 90), 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cb.setBackground(SURFACE);
        cb.setForeground(TEXT);
        cb.setPreferredSize(new Dimension(160, 32));
    }

    private void styleBtn(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(100, 32));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public String getKeyword() { return txtSearch.getText().trim(); }

    public StudentStatus getSelectedStatus() {
        int idx = cbxStatus.getSelectedIndex();
        if (idx == 0) return null;
        return StudentStatus.values()[idx - 1];
    }

    public int getSortOption() { return cbxSort.getSelectedIndex(); }

    public void addSearchListener(ActionListener l) {
        btnSearch.addActionListener(l);
        txtSearch.addActionListener(l);
    }

    public void addResetListener(ActionListener l) { btnReset.addActionListener(l); }

    public void addSortListener(ActionListener l) { cbxSort.addActionListener(l); }
    public void addFilterListener(ActionListener l) { cbxStatus.addActionListener(l); }
}
