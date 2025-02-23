package io.simpolor.mongodb2client.service;

import io.simpolor.mongodb2client.domain.Student;
import io.simpolor.mongodb2client.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public long totalCount() {
        return studentRepository.totalCount();
    }

    public List<Student> getList() {
        return studentRepository.findAll();
    }

    public Student get(String studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if(!optionalStudent.isPresent()){
            throw new NoSuchElementException("Student not found: " + studentId);
        }

        return optionalStudent.get();
    }

    public Student insert(Student student) {

        return studentRepository.save(student);
    }

    public Student update(Student student) {

        Optional<Student> optionalStudent = studentRepository.findById(student.getId());
        if(!optionalStudent.isPresent()){
            throw new NoSuchElementException("Student not found: " + student.getId());
        }

        return studentRepository.save(student);
    }

    public String delete(String studentId) {

        studentRepository.deleteById(studentId);

        return studentId;
    }

}
