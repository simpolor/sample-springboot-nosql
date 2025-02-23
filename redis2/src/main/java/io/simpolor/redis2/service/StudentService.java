package io.simpolor.redis2.service;

import io.simpolor.redis2.domain.Student;
import io.simpolor.redis2.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public long totalCount() {
        return studentRepository.count();
    }

    public List<Student> getList() {
        Iterable<Student> students = studentRepository.findAll();

        List<Student> results = new ArrayList<>();
        students.forEach(results::add);

        return results;
    }

    public Student get(String key) {
        Optional<Student> optionalStudent = studentRepository.findById(key);
        if(!optionalStudent.isPresent()){
            throw new NoSuchElementException("Student not found: " + key);
        }

        return optionalStudent.get();
    }

    public Student insert(Student student) {
        return studentRepository.save(student);
    }

    public Student modify(Student student) {

        Optional<Student> optionalStudent = studentRepository.findById(student.getKey());
        if(!optionalStudent.isPresent()){
            throw new NoSuchElementException("Student not found: " + student.getKey());
        }
        return studentRepository.save(student);
    }

    public String delete(String key) {
        studentRepository.deleteById(key);
        return key;
    }

}
