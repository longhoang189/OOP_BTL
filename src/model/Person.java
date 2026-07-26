package model;

public abstract class Person {
    private String hoTen;
    private String ngaySinh;
    private String email;

    public Person(String hoTen, String ngaySinh, String email) {
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.email = email;
    }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public abstract String getInfo();

    @Override
    public String toString() {
        return hoTen + " | " + ngaySinh + " | " + email;
    }
}
