package io.simpolor.mongo.service;

import io.simpolor.mongo.domain.Student;
import io.simpolor.mongo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public long getStudentTotalCount() {
        return studentRepository.count();
    }

    public List<Student> getStudentList() {
        return studentRepository.findAll();
    }

    public Student getStudent(String id) {
        return studentRepository.findById(id).orElse(new Student());
    }

    public Student registerStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student modifyStudent(Student student) {
        if(studentRepository.findById(student.getId()).isPresent()){
            return studentRepository.save(student);
        }
        return new Student();
    }

    public String deleteStudent(String id) {
        studentRepository.deleteById(id);
        return id;
    }

}
