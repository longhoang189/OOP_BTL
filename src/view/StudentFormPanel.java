package view;

import model.Student;
import model.StudentStatus;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StudentFormPanel extends JPanel {
    private final JTextField txtMssv   = createField();
    private final JTextField txtHoTen  = createField();
    private final JTextField txtLop    = createField();
    private final JTextField txtNgay   = createField();
    private final JTextField txtGpa    = createField();
    private final JTextField txtEmail  = createField();
    private final JComboBox<StudentStatus> cbxStatus = new JComboBox<>(StudentStatus.values());

    private final JButton btnAdd    = createBtn("Thêm",     new Color(99, 102, 241));
    private final JButton btnUpdate = createBtn("Cập nhật", new Color(16, 185, 129));
    private final JButton btnDelete = createBtn("Xóa",      new Color(239, 68, 68));
    private final JButton btnClear  = createBtn("Làm mới",  new Color(55, 65, 81));

    private static final Color BG = new Color(20, 20, 38);
    private static final Color SURFACE = new Color(28, 28, 50);
    private static final Color TEXT = new Color(210, 210, 235);
    private static final Color LABEL_COLOR = new Color(148, 163, 184);

    public StudentFormPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JLabel title = new JLabel("Thông tin sinh viên");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(new Color(165, 180, 252));
        title.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(SURFACE);
        form.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 4, 5, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        String[] labels = {"MSSV *", "Họ tên *", "Lớp *", "Ngày sinh *", "GPA *", "Trạng thái", "Email *"};
        Component[] fields = {txtMssv, txtHoTen, txtLop, txtNgay, txtGpa, cbxStatus, txtEmail};

        styleComboBox(cbxStatus);
        txtNgay.setToolTipText("Định dạng: dd/MM/yyyy");

        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i;
            gbc.gridx = 0;
            gbc.weightx = 0;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lbl.setForeground(LABEL_COLOR);
            form.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            form.add(fields[i], gbc);
        }

        add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setBackground(BG);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JTextField createField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBackground(new Color(35, 35, 60));
        f.setForeground(TEXT);
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 90), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private JButton createBtn(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 36));
        return btn;
    }

    private void styleComboBox(JComboBox<StudentStatus> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBackground(new Color(35, 35, 60));
        cb.setForeground(TEXT);
        cb.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 90), 1));
    }

    public void fill(Student s) {
        txtMssv.setText(s.getMssv());
        txtMssv.setEditable(false);
        txtHoTen.setText(s.getHoTen());
        txtLop.setText(s.getLop());
        txtNgay.setText(s.getNgaySinh());
        txtGpa.setText(String.valueOf(s.getGpa()));
        cbxStatus.setSelectedItem(s.getTrangThai());
        txtEmail.setText(s.getEmail());
    }

    public void clear() {
        txtMssv.setText("");
        txtMssv.setEditable(true);
        txtHoTen.setText("");
        txtLop.setText("");
        txtNgay.setText("");
        txtGpa.setText("");
        cbxStatus.setSelectedIndex(0);
        txtEmail.setText("");
    }

    public String getMssv()    { return txtMssv.getText().trim(); }
    public String getHoTen()   { return txtHoTen.getText().trim(); }
    public String getLop()     { return txtLop.getText().trim(); }
    public String getNgaySinh(){ return txtNgay.getText().trim(); }
    public String getGpa()     { return txtGpa.getText().trim(); }
    public String getEmail()   { return txtEmail.getText().trim(); }
    public StudentStatus getStatus() { return (StudentStatus) cbxStatus.getSelectedItem(); }

    public void addAddListener(ActionListener l)    { btnAdd.addActionListener(l); }
    public void addUpdateListener(ActionListener l) { btnUpdate.addActionListener(l); }
    public void addDeleteListener(ActionListener l) { btnDelete.addActionListener(l); }
    public void addClearListener(ActionListener l)  { btnClear.addActionListener(l); }
}
