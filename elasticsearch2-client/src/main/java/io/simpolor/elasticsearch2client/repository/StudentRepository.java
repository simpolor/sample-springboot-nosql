package io.simpolor.elasticsearch2client.repository;

import io.simpolor.elasticsearch2client.repository.entity.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends ElasticsearchRepository<Student, String>, StudentRepositoryCustom {


}
