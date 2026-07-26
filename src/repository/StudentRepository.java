package repository;

import model.Student;
import util.AppLogger;
import util.CsvUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class StudentRepository {
    private static final String FILE_PATH = "data/students.csv";
    private static final Logger log = AppLogger.get();
    private final Map<String, Student> store = new LinkedHashMap<>();

    public StudentRepository() {
        load();
    }

    private void load() {
        try {
            List<Student> list = CsvUtil.readAll(FILE_PATH);
            for (Student s : list) {
                store.put(s.getMssv().toLowerCase(), s);
            }
            log.info("Đã tải " + store.size() + " sinh viên từ " + FILE_PATH);
        } catch (IOException e) {
            log.warning("Không thể đọc file dữ liệu: " + e.getMessage());
        }
    }

    public void save() {
        try {
            CsvUtil.writeAll(FILE_PATH, new ArrayList<>(store.values()));
            log.fine("Đã lưu " + store.size() + " sinh viên vào " + FILE_PATH);
        } catch (IOException e) {
            log.warning("Không thể ghi file dữ liệu: " + e.getMessage());
        }
    }

    public boolean existsById(String mssv) {
        return store.containsKey(mssv.toLowerCase());
    }

    public void add(Student student) {
        store.put(student.getMssv().toLowerCase(), student);
        save();
        log.info("Thêm sinh viên: " + student.getMssv() + " - " + student.getHoTen());
    }

    public void update(Student student) {
        store.put(student.getMssv().toLowerCase(), student);
        save();
        log.info("Cập nhật sinh viên: " + student.getMssv() + " - " + student.getHoTen());
    }

    public void delete(String mssv) {
        store.remove(mssv.toLowerCase());
        save();
        log.info("Xóa sinh viên: " + mssv);
    }

    public Student findById(String mssv) {
        return store.get(mssv.toLowerCase());
    }

    public List<Student> findAll() {
        return new ArrayList<>(store.values());
    }
}
