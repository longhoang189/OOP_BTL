package model;

public enum StudentStatus {
    DANG_HOC("Đang học"),
    TOT_NGHIEP("Tốt nghiệp"),
    BAO_LUU("Bảo lưu"),
    DINH_CHI("Đình chỉ");

    private final String label;

    StudentStatus(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }

    public static StudentStatus fromLabel(String label) {
        for (StudentStatus s : values()) {
            if (s.label.equals(label)) return s;
        }
        return DANG_HOC;
    }

    @Override
    public String toString() { return label; }
}
