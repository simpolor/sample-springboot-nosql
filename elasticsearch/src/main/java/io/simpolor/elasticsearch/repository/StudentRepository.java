package io.simpolor.elasticsearch.repository;

import io.simpolor.elasticsearch.repository.entity.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends ElasticsearchRepository<Student, String>, CustomRepository {


}
