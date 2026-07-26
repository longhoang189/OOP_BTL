package model;

public class Student extends Person {
    private String mssv;
    private String lop;
    private double gpa;
    private StudentStatus trangThai;

    public Student(String mssv, String hoTen, String lop, String ngaySinh,
                   double gpa, StudentStatus trangThai, String email) {
        super(hoTen, ngaySinh, email);
        this.mssv = mssv;
        this.lop = lop;
        this.gpa = gpa;
        this.trangThai = trangThai;
    }

    public String getMssv() { return mssv; }
    public void setMssv(String mssv) { this.mssv = mssv; }

    public String getLop() { return lop; }
    public void setLop(String lop) { this.lop = lop; }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public StudentStatus getTrangThai() { return trangThai; }
    public void setTrangThai(StudentStatus trangThai) { this.trangThai = trangThai; }

    @Override
    public String getInfo() {
        return String.format("MSSV: %s | Họ tên: %s | Lớp: %s | GPA: %.2f | Trạng thái: %s",
                mssv, getHoTen(), lop, gpa, trangThai.getLabel());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Student)) return false;
        Student other = (Student) obj;
        return mssv.equalsIgnoreCase(other.mssv);
    }

    @Override
    public int hashCode() {
        return mssv.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return getInfo();
    }
}
