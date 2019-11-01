package io.simpolor.elasticsearch.repository;

import io.simpolor.elasticsearch.domain.Student;

public interface CustomRepository {

    Student update(Student student);

}
