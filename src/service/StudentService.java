package service;

import exception.DuplicateIdException;
import exception.ValidationException;
import model.Student;
import model.StudentStatus;

import java.util.List;
import java.util.Map;

public interface StudentService {
    void addStudent(Student student) throws ValidationException, DuplicateIdException;
    void updateStudent(Student student) throws ValidationException;
    void deleteStudent(String mssv) throws ValidationException;
    Student findById(String mssv);
    List<Student> findAll();
    List<Student> search(String keyword);
    List<Student> filterByStatus(StudentStatus status);
    List<Student> sortByName(boolean ascending);
    List<Student> sortByGpa(boolean ascending);
    Map<String, double[]> statisticsByClass();
}
