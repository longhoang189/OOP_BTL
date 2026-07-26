package util;

import exception.ValidationException;

public class ValidationUtil {

    public static void validateMssv(String mssv) throws ValidationException {
        if (mssv == null || mssv.trim().isEmpty()) {
            throw new ValidationException("MSSV không được để trống.");
        }
        if (!mssv.trim().matches("[A-Za-z0-9]+")) {
            throw new ValidationException("MSSV chỉ được chứa chữ cái và số.");
        }
    }

    public static void validateHoTen(String hoTen) throws ValidationException {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new ValidationException("Họ tên không được để trống.");
        }
        if (hoTen.trim().length() < 2) {
            throw new ValidationException("Họ tên phải có ít nhất 2 ký tự.");
        }
    }

    public static void validateLop(String lop) throws ValidationException {
        if (lop == null || lop.trim().isEmpty()) {
            throw new ValidationException("Lớp không được để trống.");
        }
    }

    public static double validateGpa(String gpaStr) throws ValidationException {
        if (gpaStr == null || gpaStr.trim().isEmpty()) {
            throw new ValidationException("GPA không được để trống.");
        }
        double gpa;
        try {
            gpa = Double.parseDouble(gpaStr.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new ValidationException("GPA phải là số hợp lệ (0.0 - 4.0).");
        }
        if (gpa < 0.0 || gpa > 4.0) {
            throw new ValidationException("GPA phải trong khoảng 0.0 đến 4.0.");
        }
        return gpa;
    }

    public static void validateNgaySinh(String ngaySinh) throws ValidationException {
        if (ngaySinh == null || ngaySinh.trim().isEmpty()) {
            throw new ValidationException("Ngày sinh không được để trống.");
        }
        if (!ngaySinh.trim().matches("\\d{2}/\\d{2}/\\d{4}")) {
            throw new ValidationException("Ngày sinh phải có định dạng dd/MM/yyyy.");
        }
    }

    public static void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email không được để trống.");
        }
        if (!email.trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidationException("Email không hợp lệ.");
        }
    }
}
