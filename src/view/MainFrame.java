package view;

import controller.StudentController;
import model.Student;
import model.StudentStatus;
import util.AppLogger;
import util.CsvImportExport;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Logger;

public class MainFrame extends JFrame {
    private static final Color BG         = new Color(15, 15, 30);
    private static final Color SIDEBAR_BG = new Color(12, 12, 25);
    private static final Color ACCENT     = new Color(99, 102, 241);
    private static final Logger log = AppLogger.get();

    private final StudentController controller;
    private final StudentTablePanel tablePanel  = new StudentTablePanel();
    private final StudentFormPanel  formPanel   = new StudentFormPanel();
    private final SearchPanel       searchPanel = new SearchPanel();
    private final StatisticsPanel   statsPanel  = new StatisticsPanel();

    public MainFrame(StudentController controller) {
        this.controller = controller;
        controller.setView(this);
        log.info("=== Ứng dụng khởi động ===");
        initUI();
        refreshTable(controller.loadAll());
        refreshStats();
    }

    private void initUI() {
        setTitle("Quản lý Sinh Viên - OOP BTL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setMinimumSize(new Dimension(900, 550));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        add(buildMenuBar_asPanel(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(BG);
        tabs.setForeground(new Color(165, 180, 252));
        tabs.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                highlight = BG;
                lightHighlight = BG;
                shadow = BG;
                darkShadow = BG;
                focus = ACCENT;
            }
        });

        tabs.addTab("Danh sách sinh viên", buildListTab());
        tabs.addTab("Thống kê", statsPanel);
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 1) refreshStats();
        });

        add(tabs, BorderLayout.CENTER);

        bindActions();
    }

    private JPanel buildMenuBar_asPanel() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(SIDEBAR_BG);
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(40, 40, 70)));

        JLabel logo = new JLabel("  Student Manager");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logo.setForeground(ACCENT);
        logo.setBorder(new EmptyBorder(12, 16, 12, 16));
        bar.add(logo, BorderLayout.WEST);

        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        eastPanel.setBackground(SIDEBAR_BG);

        JButton btnImport = createHeaderBtn("Import CSV", new Color(16, 185, 129));
        JButton btnExport = createHeaderBtn("Export CSV", new Color(245, 158, 11));

        btnImport.addActionListener(e -> {
            List<Student> list = CsvImportExport.importCsv(this);
            if (list != null && !list.isEmpty()) {
                controller.importStudents(list);
            }
        });
        btnExport.addActionListener(e -> controller.exportStudents());

        eastPanel.add(btnImport);
        eastPanel.add(btnExport);

        JLabel info = new JLabel("Java Swing MVC  |  OOP BTL  ");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        info.setForeground(new Color(100, 100, 130));
        eastPanel.add(info);

        bar.add(eastPanel, BorderLayout.EAST);
        return bar;
    }

    private JButton createHeaderBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 28));
        return btn;
    }

    private JPanel buildListTab() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG);

        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(BG);
        left.add(searchPanel, BorderLayout.NORTH);
        left.add(tablePanel, BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(new Color(20, 20, 38));
        right.setPreferredSize(new Dimension(290, 0));
        right.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(40, 40, 70)));
        right.add(formPanel, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setDividerLocation(880);
        split.setDividerSize(2);
        split.setBackground(BG);
        split.setBorder(null);

        p.add(split, BorderLayout.CENTER);
        return p;
    }

    private void bindActions() {
        tablePanel.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String mssv = tablePanel.getSelectedMssv();
                if (mssv != null) {
                    Student s = controller.findById(mssv);
                    if (s != null) formPanel.fill(s);
                }
            }
        });

        formPanel.addAddListener(e -> {
            controller.addStudent(
                    formPanel.getMssv(), formPanel.getHoTen(), formPanel.getLop(),
                    formPanel.getNgaySinh(), formPanel.getGpa(), formPanel.getStatus(),
                    formPanel.getEmail());
            formPanel.clear();
        });

        formPanel.addUpdateListener(e -> {
            String mssv = formPanel.getMssv();
            if (mssv.isEmpty()) { showError("Vui lòng chọn sinh viên cần cập nhật."); return; }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Cập nhật sinh viên " + mssv + "?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.updateStudent(
                        mssv, formPanel.getHoTen(), formPanel.getLop(),
                        formPanel.getNgaySinh(), formPanel.getGpa(), formPanel.getStatus(),
                        formPanel.getEmail());
                formPanel.clear();
            }
        });

        formPanel.addDeleteListener(e -> {
            String mssv = formPanel.getMssv();
            if (mssv.isEmpty()) mssv = tablePanel.getSelectedMssv();
            if (mssv == null || mssv.isEmpty()) { showError("Vui lòng chọn sinh viên cần xóa."); return; }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Xóa sinh viên " + mssv + "? Không thể hoàn tác.",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.deleteStudent(mssv);
                formPanel.clear();
            }
        });

        formPanel.addClearListener(e -> formPanel.clear());

        ActionListener doSearch = e -> applyFilters();
        searchPanel.addSearchListener(doSearch);
        searchPanel.addFilterListener(doSearch);
        searchPanel.addSortListener(doSearch);
        searchPanel.addResetListener(e -> {
            refreshTable(controller.loadAll());
        });
    }

    private void applyFilters() {
        String kw = searchPanel.getKeyword();
        StudentStatus status = searchPanel.getSelectedStatus();
        int sortOpt = searchPanel.getSortOption();

        List<Student> list;
        if (!kw.isEmpty()) {
            list = controller.search(kw);
        } else if (status != null) {
            list = controller.filterByStatus(status);
        } else {
            list = controller.loadAll();
        }

        if (!kw.isEmpty() && status != null) {
            list = controller.filterByStatus(status);
            final String kwf = kw.toLowerCase();
            java.util.List<Student> filtered = new java.util.ArrayList<>();
            for (Student s : list) {
                if (s.getMssv().toLowerCase().contains(kwf)
                        || s.getHoTen().toLowerCase().contains(kwf)
                        || s.getLop().toLowerCase().contains(kwf)) {
                    filtered.add(s);
                }
            }
            list = filtered;
        }

        switch (sortOpt) {
            case 1: list = sortByName(list, true);  break;
            case 2: list = sortByName(list, false); break;
            case 3: list = sortByGpa(list, false);  break;
            case 4: list = sortByGpa(list, true);   break;
            default: break;
        }
        tablePanel.populate(list);
    }

    private List<Student> sortByName(List<Student> list, boolean asc) {
        List<Student> copy = new java.util.ArrayList<>(list);
        copy.sort((a, b) -> asc
                ? a.getHoTen().compareToIgnoreCase(b.getHoTen())
                : b.getHoTen().compareToIgnoreCase(a.getHoTen()));
        return copy;
    }

    private List<Student> sortByGpa(List<Student> list, boolean asc) {
        List<Student> copy = new java.util.ArrayList<>(list);
        copy.sort((a, b) -> asc
                ? Double.compare(a.getGpa(), b.getGpa())
                : Double.compare(b.getGpa(), a.getGpa()));
        return copy;
    }

    public void refreshTable(List<Student> list) {
        tablePanel.populate(list);
    }

    private void refreshStats() {
        statsPanel.update(controller.getStatistics(), controller.loadAll().size());
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
}
