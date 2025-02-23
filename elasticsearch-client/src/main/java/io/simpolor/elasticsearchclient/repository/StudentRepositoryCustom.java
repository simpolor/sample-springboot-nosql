package io.simpolor.elasticsearchclient.repository;


import io.simpolor.elasticsearchclient.repository.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepositoryCustom {

    long totalCount();

    List<Student> list();

    SearchAfter<Student> searchAfter(String searchAfter, Integer size);

    Optional<Student> get(String studentId);

    Student insert(Student student);

    Student update(Student student);

    void delete(String studentId);

}
