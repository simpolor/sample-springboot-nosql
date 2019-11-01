package io.simpolor.elasticsearch.service;

import io.simpolor.elasticsearch.domain.Student;
import io.simpolor.elasticsearch.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public long getStudentTotalCount() {
        return studentRepository.count();
    }

    public List<Student> getStudentList() {
        List<Student> list = new ArrayList<>();
        studentRepository.findAll().forEach(list::add);
        return list;
    }

    public Student getStudent(String id) {
        return studentRepository.findById(id).orElse(new Student()) ;
    }

    public Student registerStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student modifyStudent(Student student) {
        if(studentRepository.findById(student.getId()).isPresent()){
            return studentRepository.update(student);
        }
        return new Student();
    }

    public String deleteStudent(String id) {
        studentRepository.deleteById(id);
        return id;
    }

}
