package util;

import model.Student;
import model.StudentStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    private static final String HEADER = "mssv,hoTen,lop,ngaySinh,gpa,trangThai,email";

    public static void writeAll(String filePath, List<Student> students) throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, false), StandardCharsets.UTF_8))) {
            writer.write(HEADER);
            writer.newLine();
            for (Student s : students) {
                writer.write(toCsv(s));
                writer.newLine();
            }
        }
    }

    public static List<Student> readAll(String filePath) throws IOException {
        List<Student> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return list;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Student s = fromCsv(line);
                if (s != null) list.add(s);
            }
        }
        return list;
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
            String mssv = unescape(parts[0]);
            String hoTen = unescape(parts[1]);
            String lop = unescape(parts[2]);
            String ngaySinh = unescape(parts[3]);
            double gpa = Double.parseDouble(parts[4].trim());
            StudentStatus trangThai = StudentStatus.fromLabel(unescape(parts[5]));
            String email = unescape(parts[6]);
            return new Student(mssv, hoTen, lop, ngaySinh, gpa, trangThai, email);
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
