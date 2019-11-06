package io.simpolor.mongo.service;

import io.simpolor.mongo.domain.Student;
import io.simpolor.mongo.repository.MgStudentRepository;
import io.simpolor.mongo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MgStudentService {

    @Autowired
    private MgStudentRepository mgStudentRepository;

    public long getStudentTotalCount() {
        return mgStudentRepository.count();
    }

    public List<Student> getStudentList() {
        return mgStudentRepository.findAll();
    }

    public Student getStudent(String id) {
        return mgStudentRepository.findById(id).orElse(new Student());
    }

    public Student registerStudent(Student student) {
        return mgStudentRepository.save(student);
    }

    public Student modifyStudent(Student student) {
        if(mgStudentRepository.findById(student.getId()).isPresent()){
            return mgStudentRepository.save(student);
        }
        return new Student();
    }

    public String deleteStudent(String id) {
        mgStudentRepository.deleteById(id);
        return id;
    }

}
