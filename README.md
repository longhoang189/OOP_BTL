# Quản lý Sinh Viên - OOP BTL

Ứng dụng Desktop **Quản lý Sinh Viên** xây dựng bằng **Java Swing**, áp dụng kiến trúc **MVC** và đầy đủ 4 nguyên lý **OOP**.

> Bài tập lớn môn Lập trình Hướng đối tượng — Học kỳ 3, 2025–2026

---

## Tính năng

| Module | Chức năng |
|---|---|
| Quản lý sinh viên | Thêm / Sửa / Xóa / Hiển thị danh sách |
| Tìm kiếm & Lọc | Tìm toàn văn, lọc theo trạng thái, sắp xếp GPA/tên |
| Thống kê | Tổng SV, GPA TB/max, thống kê theo lớp |
| Import CSV | Nhập dữ liệu từ file CSV, báo lỗi từng dòng |
| Export CSV | Xuất danh sách ra file CSV (UTF-8 BOM, mở được bằng Excel) |
| Logging | Ghi nhật ký hoạt động ra `logs/app.log` với rotation |

---

## Kiến trúc

```
src/
├── Main.java
├── model/          # Person (abstract), Student, StudentStatus (enum)
├── view/           # MainFrame, StudentTablePanel, StudentFormPanel, SearchPanel, StatisticsPanel
├── controller/     # StudentController
├── service/        # StudentService (interface), StudentServiceImpl
├── repository/     # StudentRepository (đọc/ghi CSV)
├── util/           # ValidationUtil, CsvUtil, CsvImportExport, AppLogger
└── exception/      # ValidationException, DuplicateIdException
```

### OOP áp dụng

| Nguyên lý | Ví dụ trong code |
|---|---|
| Đóng gói | Mọi field đều `private`, truy xuất qua getter/setter |
| Trừu tượng | `Person` (abstract class), `StudentService` (interface) |
| Kế thừa | `Student extends Person` |
| Đa hình | `Student.getInfo()` override, `StudentServiceImpl implements StudentService` |

---

## Yêu cầu

- **JDK 7+** (khuyến nghị JDK 11+)
- Không cần cài thêm thư viện bên thứ ba

---

## Cách chạy

### Dùng file batch (Windows)

```bat
cd /d D:\Java\BTL
.\build_and_run.bat
```

File `build_and_run.bat` sẽ tự động biên dịch toàn bộ `src/` rồi chạy ứng dụng.

### Dùng IDE (IntelliJ IDEA / Eclipse / NetBeans)

1. Mở IDE → **File → Open Project** → chọn thư mục project
2. Đặt `src/` là **Sources Root**
3. Mở `src/Main.java` → nhấn **Run**

### Dùng lệnh thủ công

```bash
# Biên dịch
javac -encoding UTF-8 -d out $(find src -name "*.java")

# Chạy
java -cp out Main
```

> **Lưu ý:** Thư mục `data/` và `logs/` được tạo tự động khi chạy lần đầu.

---

## Cấu trúc dữ liệu

Dữ liệu sinh viên lưu tại `data/students.csv` (UTF-8 BOM):

```
mssv,hoTen,lop,ngaySinh,gpa,trangThai,email
SV001,Nguyễn Văn An,CNTT01,15/03/2004,3.75,Đang học,an.nguyen@example.com
```

---

## Giao diện

| Tab | Mô tả |
|---|---|
| Danh sách sinh viên | Bảng JTable với màu GPA, form nhập liệu, thanh tìm kiếm |
| Thống kê | Thẻ tổng quan + bảng thống kê theo lớp |

---

## Tác giả

**Nguyễn Văn Long** — PTIT  
Giảng viên hướng dẫn: Nguyễn Đình Quảng
