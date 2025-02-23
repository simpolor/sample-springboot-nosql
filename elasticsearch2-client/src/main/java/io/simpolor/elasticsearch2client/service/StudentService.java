package io.simpolor.elasticsearch2client.service;

import io.simpolor.elasticsearch2client.repository.SearchAfter;
import io.simpolor.elasticsearch2client.repository.StudentRepository;
import io.simpolor.elasticsearch2client.repository.entity.Student;
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
        Iterable<Student> students = studentRepository.list();
        List<Student> list = new ArrayList<>();
        for(Student student : students){
            list.add(student);
        }

        return list;
    }

    public SearchAfter<Student> getAll(String searchAfter, Integer size) {
        return studentRepository.searchAfter(searchAfter, size);
    }

    public Student get(String id) {
        Optional<Student> optionalStudent = studentRepository.get(id);
        if(!optionalStudent.isPresent()){
            throw new IllegalArgumentException("id : "+id);
        }

        return optionalStudent.get();
    }

    public Student create(Student student) {

        return studentRepository.insert(student);
    }

    public Student update(Student student) {
        Optional<Student> optionalStudent = studentRepository.findById(student.getId());
        if(!optionalStudent.isPresent()){
            throw new IllegalArgumentException("id : "+student.getId());
        }

        return studentRepository.update(student);
    }

    public String delete(String studentId) {

        studentRepository.delete(studentId);

        return studentId;
    }

}
