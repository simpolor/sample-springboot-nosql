package io.simpolor.elasticsearch.service;

import io.simpolor.elasticsearch.domain.SearchAfter;
import io.simpolor.elasticsearch.domain.Student;
import io.simpolor.elasticsearch.repository.SearchAfterStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchAfterStudentService {

    @Autowired
    private SearchAfterStudentRepository afterStudentRepository;

    public long getStudentTotalCount() {
        return afterStudentRepository.selectStudentTotalCount();
    }

    public SearchAfter<Student> getStudentList(String searchAfter, int size) {
        return afterStudentRepository.selectStudentList(searchAfter, size);
    }

    public Student getStudent(String id) {
        if(afterStudentRepository.selectStudent(id) != null){
            return afterStudentRepository.selectStudent(id);
        }
        return new Student();
    }

    public Student registerStudent(Student student) {
        afterStudentRepository.insertStudent(student);
        //if(esStudentRepository.insertStudent(student).){
            return student;
        //}
       // return new Student();
    }

    public Student modifyStudent(Student student) {
        afterStudentRepository.updateStudent(student);
       // if(esStudentRepository.updateStudent(student) > 0){
            return student;
        //}
       // return new Student();
    }

    public String deleteStudent(String id) {
        afterStudentRepository.deleteStudent(id);
        return id;
    }

}
