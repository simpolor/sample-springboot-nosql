package io.simpolor.elasticsearch.service;

import io.simpolor.elasticsearch.repository.StudentRepository;
import io.simpolor.elasticsearch.repository.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAll() {
        Iterable<Student> students = studentRepository.findAll();
        List<Student> list = new ArrayList<>();
        for(Student student : students){
            list.add(student);
        }

        return list;
    }

    public Student get(String id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if(!optionalStudent.isPresent()){
            throw new IllegalArgumentException("id : "+id);
        }

        return optionalStudent.get();
    }

    public Student create(Student student) {

        return studentRepository.save(student);
    }

    public Student update(Student student) {
        Optional<Student> optionalStudent = studentRepository.findById(student.getId());
        if(!optionalStudent.isPresent()){
            throw new IllegalArgumentException("id : "+student.getId());
        }

        return studentRepository.save(student);
    }

    public Student update2(Student student) {
        Optional<Student> optionalStudent = studentRepository.findById(student.getId());
        if(!optionalStudent.isPresent()){
            throw new IllegalArgumentException("id : "+student.getId());
        }

        return studentRepository.update(student);
    }

    public String delete(String id) {

        studentRepository.deleteById(id);

        return id;
    }

}
