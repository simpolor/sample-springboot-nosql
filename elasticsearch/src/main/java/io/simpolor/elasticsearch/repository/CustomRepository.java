package io.simpolor.elasticsearch.repository;

import io.simpolor.elasticsearch.repository.entity.Student;

public interface CustomRepository {

    Student update(Student student);

}
