package util;

import model.Student;
import model.StudentStatus;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CsvImportExport {
    private static final Logger log = AppLogger.get();
    private static final String HEADER = "mssv,hoTen,lop,ngaySinh,gpa,trangThai,email";

    public static File chooseFile(Component parent, boolean save) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("CSV files (*.csv)", "csv"));
        fc.setDialogTitle(save ? "Xuất danh sách ra file CSV" : "Nhập danh sách từ file CSV");
        if (save) {
            fc.setSelectedFile(new File("danh_sach_sinh_vien.csv"));
            int result = fc.showSaveDialog(parent);
            if (result != JFileChooser.APPROVE_OPTION) return null;
            File f = fc.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".csv")) {
                f = new File(f.getAbsolutePath() + ".csv");
            }
            return f;
        } else {
            int result = fc.showOpenDialog(parent);
            return result == JFileChooser.APPROVE_OPTION ? fc.getSelectedFile() : null;
        }
    }

    public static void exportCsv(Component parent, List<Student> students) {
        File file = chooseFile(parent, true);
        if (file == null) return;
        try (BufferedWriter w = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            w.write('\ufeff'); // BOM để Excel mở đúng tiếng Việt
            w.write(HEADER);
            w.newLine();
            for (Student s : students) {
                w.write(toCsv(s));
                w.newLine();
            }
            log.info("Export CSV: " + students.size() + " sinh viên -> " + file.getAbsolutePath());
            JOptionPane.showMessageDialog(parent,
                    "Đã xuất " + students.size() + " sinh viên\nFile: " + file.getName(),
                    "Xuất CSV thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            log.warning("Export CSV thất bại: " + e.getMessage());
            JOptionPane.showMessageDialog(parent, "Lỗi xuất file: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static List<Student> importCsv(Component parent) {
        File file = chooseFile(parent, false);
        if (file == null) return null;

        List<Student> list = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = r.readLine();
            if (line == null) {
                JOptionPane.showMessageDialog(parent, "File rỗng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            // bỏ BOM nếu có
            if (line.startsWith("\ufeff")) line = line.substring(1);
            // bỏ dòng header
            int lineNum = 1;
            while ((line = r.readLine()) != null) {
                lineNum++;
                if (line.trim().isEmpty()) continue;
                Student s = fromCsv(line);
                if (s == null) {
                    errors.add("Dòng " + lineNum + ": dữ liệu không hợp lệ");
                } else {
                    list.add(s);
                }
            }
        } catch (IOException e) {
            log.warning("Import CSV thất bại: " + e.getMessage());
            JOptionPane.showMessageDialog(parent, "Lỗi đọc file: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        StringBuilder msg = new StringBuilder();
        msg.append("Đọc được ").append(list.size()).append(" sinh viên hợp lệ");
        if (!errors.isEmpty()) {
            msg.append("\nBỏ qua ").append(errors.size()).append(" dòng lỗi:");
            for (String err : errors) msg.append("\n  - ").append(err);
        }

        log.info("Import CSV: " + list.size() + " hợp lệ, " + errors.size() + " lỗi <- " + file.getAbsolutePath());

        int confirm = JOptionPane.showConfirmDialog(parent,
                msg + "\n\nTiếp tục nhập?",
                "Xác nhận Import", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return confirm == JOptionPane.YES_OPTION ? list : null;
    }

    private static String toCsv(Student s) {
        return String.join(",",
                escape(s.getMssv()),
                escape(s.getHoTen()),
                escape(s.getLop()),
                escape(s.getNgaySinh()),
                String.valueOf(s.getGpa()),
                escape(s.getTrangThai().getLabel()),
                escape(s.getEmail()));
    }

    private static Student fromCsv(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 7) return null;
        try {
            String mssv     = unescape(parts[0]);
            String hoTen    = unescape(parts[1]);
            String lop      = unescape(parts[2]);
            String ngaySinh = unescape(parts[3]);
            double gpa      = Double.parseDouble(parts[4].trim());
            StudentStatus tt = StudentStatus.fromLabel(unescape(parts[5]));
            String email    = unescape(parts[6]);
            if (mssv.isEmpty() || hoTen.isEmpty()) return null;
            return new Student(mssv, hoTen, lop, ngaySinh, gpa, tt, email);
        } catch (Exception e) {
            return null;
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    private static String unescape(String s) {
        if (s == null) return "";
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length() - 1).replace("\"\"", "\"");
        }
        return s;
    }
}
