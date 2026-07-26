package controller;

import exception.DuplicateIdException;
import exception.ValidationException;
import model.Student;
import model.StudentStatus;
import service.StudentService;
import util.AppLogger;
import util.CsvImportExport;
import util.ValidationUtil;
import view.MainFrame;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class StudentController {
    private static final Logger log = AppLogger.get();
    private final StudentService service;
    private MainFrame view;

    public StudentController(StudentService service) {
        this.service = service;
    }

    public void setView(MainFrame view) {
        this.view = view;
    }

    public List<Student> loadAll() {
        return service.findAll();
    }

    public void addStudent(String mssv, String hoTen, String lop, String ngaySinh,
                           String gpaStr, StudentStatus status, String email) {
        try {
            ValidationUtil.validateMssv(mssv);
            ValidationUtil.validateHoTen(hoTen);
            ValidationUtil.validateLop(lop);
            ValidationUtil.validateNgaySinh(ngaySinh);
            double gpa = ValidationUtil.validateGpa(gpaStr);
            ValidationUtil.validateEmail(email);
            Student s = new Student(mssv.trim(), hoTen.trim(), lop.trim(),
                    ngaySinh.trim(), gpa, status, email.trim());
            service.addStudent(s);
            view.showSuccess("Thêm sinh viên thành công!");
            view.refreshTable(service.findAll());
        } catch (ValidationException | DuplicateIdException e) {
            log.warning("Thêm thất bại [" + mssv + "]: " + e.getMessage());
            view.showError(e.getMessage());
        }
    }

    public void updateStudent(String mssv, String hoTen, String lop, String ngaySinh,
                              String gpaStr, StudentStatus status, String email) {
        try {
            ValidationUtil.validateMssv(mssv);
            ValidationUtil.validateHoTen(hoTen);
            ValidationUtil.validateLop(lop);
            ValidationUtil.validateNgaySinh(ngaySinh);
            double gpa = ValidationUtil.validateGpa(gpaStr);
            ValidationUtil.validateEmail(email);
            Student s = new Student(mssv.trim(), hoTen.trim(), lop.trim(),
                    ngaySinh.trim(), gpa, status, email.trim());
            service.updateStudent(s);
            view.showSuccess("Cập nhật sinh viên thành công!");
            view.refreshTable(service.findAll());
        } catch (ValidationException e) {
            log.warning("Cập nhật thất bại [" + mssv + "]: " + e.getMessage());
            view.showError(e.getMessage());
        }
    }

    public void deleteStudent(String mssv) {
        try {
            service.deleteStudent(mssv);
            view.showSuccess("Xóa sinh viên thành công!");
            view.refreshTable(service.findAll());
        } catch (ValidationException e) {
            log.warning("Xóa thất bại [" + mssv + "]: " + e.getMessage());
            view.showError(e.getMessage());
        }
    }

    public List<Student> search(String keyword) {
        return service.search(keyword);
    }

    public List<Student> filterByStatus(StudentStatus status) {
        return service.filterByStatus(status);
    }

    public List<Student> sortByName(boolean asc) {
        return service.sortByName(asc);
    }

    public List<Student> sortByGpa(boolean asc) {
        return service.sortByGpa(asc);
    }

    public Map<String, double[]> getStatistics() {
        return service.statisticsByClass();
    }

    public Student findById(String mssv) {
        return service.findById(mssv);
    }

    public void importStudents(java.util.List<Student> students) {
        int added = 0, skipped = 0;
        for (Student s : students) {
            try {
                service.addStudent(s);
                added++;
            } catch (exception.DuplicateIdException e) {
                skipped++;
                log.info("Import bỏ qua trùng MSSV: " + s.getMssv());
            } catch (exception.ValidationException e) {
                skipped++;
                log.warning("Import bỏ qua dữ liệu không hợp lệ [" + s.getMssv() + "]: " + e.getMessage());
            }
        }
        log.info("Import hoàn tất: " + added + " thêm mới, " + skipped + " bỏ qua");
        view.showSuccess("Import xong: thêm " + added + " sinh viên" +
                (skipped > 0 ? ", bỏ qua " + skipped + " (trùng MSSV hoặc lỗi)" : ""));
        view.refreshTable(service.findAll());
    }

    public void exportStudents() {
        util.CsvImportExport.exportCsv(view, service.findAll());
    }
}
