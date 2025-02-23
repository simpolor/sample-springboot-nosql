package io.simpolor.redis2.repository;

import io.simpolor.redis2.domain.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, String> {

}
