package io.simpolor.elasticsearch.service;

import io.simpolor.elasticsearch.domain.Student;
import io.simpolor.elasticsearch.repository.EsStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EsStudentService {

    @Autowired
    private EsStudentRepository esStudentRepository;

    public long getStudentTotalCount() {
        return esStudentRepository.selectStudentTotalCount();
    }

    public List<Student> getStudentList() {
        return esStudentRepository.selectStudentList();
    }

    public Student getStudent(String id) {
        if(esStudentRepository.selectStudent(id) != null){
            return esStudentRepository.selectStudent(id);
        }
        return new Student();
    }

    public Student registerStudent(Student student) {
        esStudentRepository.insertStudent(student);
        //if(esStudentRepository.insertStudent(student).){
            return student;
        //}
       // return new Student();
    }

    public Student modifyStudent(Student student) {
        esStudentRepository.updateStudent(student);
       // if(esStudentRepository.updateStudent(student) > 0){
            return student;
        //}
       // return new Student();
    }

    public String deleteStudent(String id) {
        esStudentRepository.deleteStudent(id);
        return id;
    }

}
