package service;

import exception.DuplicateIdException;
import exception.ValidationException;
import model.Student;
import model.StudentStatus;
import repository.StudentRepository;
import util.AppLogger;
import util.ValidationUtil;

import java.util.*;
import java.util.logging.Logger;

public class StudentServiceImpl implements StudentService {
    private static final Logger log = AppLogger.get();
    private final StudentRepository repository;

    public StudentServiceImpl(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addStudent(Student student) throws ValidationException, DuplicateIdException {
        validateStudent(student);
        if (repository.existsById(student.getMssv())) {
            throw new DuplicateIdException(student.getMssv());
        }
        repository.add(student);
    }

    @Override
    public void updateStudent(Student student) throws ValidationException {
        validateStudent(student);
        repository.update(student);
    }

    @Override
    public void deleteStudent(String mssv) throws ValidationException {
        if (mssv == null || mssv.trim().isEmpty()) {
            throw new ValidationException("MSSV không hợp lệ.");
        }
        if (!repository.existsById(mssv)) {
            throw new ValidationException("Không tìm thấy sinh viên với MSSV: " + mssv);
        }
        repository.delete(mssv);
    }

    @Override
    public Student findById(String mssv) {
        return repository.findById(mssv);
    }

    @Override
    public List<Student> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Student> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return findAll();
        String kw = keyword.trim().toLowerCase();
        List<Student> result = new ArrayList<>();
        for (Student s : repository.findAll()) {
            if (s.getMssv().toLowerCase().contains(kw)
                    || s.getHoTen().toLowerCase().contains(kw)
                    || s.getLop().toLowerCase().contains(kw)
                    || s.getEmail().toLowerCase().contains(kw)) {
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public List<Student> filterByStatus(StudentStatus status) {
        if (status == null) return findAll();
        List<Student> result = new ArrayList<>();
        for (Student s : repository.findAll()) {
            if (s.getTrangThai() == status) result.add(s);
        }
        return result;
    }

    @Override
    public List<Student> sortByName(boolean ascending) {
        List<Student> list = new ArrayList<>(repository.findAll());
        Collections.sort(list, new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                int cmp = a.getHoTen().compareToIgnoreCase(b.getHoTen());
                return ascending ? cmp : -cmp;
            }
        });
        return list;
    }

    @Override
    public List<Student> sortByGpa(boolean ascending) {
        List<Student> list = new ArrayList<>(repository.findAll());
        Collections.sort(list, new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                int cmp = Double.compare(a.getGpa(), b.getGpa());
                return ascending ? cmp : -cmp;
            }
        });
        return list;
    }

    @Override
    public Map<String, double[]> statisticsByClass() {
        Map<String, List<Double>> classGpa = new LinkedHashMap<>();
        for (Student s : repository.findAll()) {
            String lop = s.getLop();
            if (!classGpa.containsKey(lop)) classGpa.put(lop, new ArrayList<Double>());
            classGpa.get(lop).add(s.getGpa());
        }
        Map<String, double[]> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<Double>> entry : classGpa.entrySet()) {
            List<Double> gpas = entry.getValue();
            double sum = 0;
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for (double g : gpas) {
                sum += g;
                if (g < min) min = g;
                if (g > max) max = g;
            }
            double avg = sum / gpas.size();
            result.put(entry.getKey(), new double[]{gpas.size(), avg, min, max});
        }
        return result;
    }

    private void validateStudent(Student s) throws ValidationException {
        ValidationUtil.validateMssv(s.getMssv());
        ValidationUtil.validateHoTen(s.getHoTen());
        ValidationUtil.validateLop(s.getLop());
        ValidationUtil.validateNgaySinh(s.getNgaySinh());
        ValidationUtil.validateEmail(s.getEmail());
        if (s.getGpa() < 0 || s.getGpa() > 4.0) {
            throw new ValidationException("GPA phải trong khoảng 0.0 đến 4.0.");
        }
    }
}
