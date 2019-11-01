package io.simpolor.redis.service;

import com.google.common.collect.Lists;
import io.simpolor.redis.domain.Student;
import io.simpolor.redis.repository.StudentRepository;
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
        return Lists.newArrayList(studentRepository.findAll());

    }

    public Student getStudent(String key) {
        return studentRepository.findById(key).orElse(new Student());
    }

    public Student registerStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student modifyStudent(Student student) {
        if(studentRepository.findById(student.getKey()).isPresent()){
            return studentRepository.save(student);
        }
        return new Student();
    }

    public String deleteStudent(String key) {
        studentRepository.deleteById(key);
        return key;
    }

}
