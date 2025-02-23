package io.simpolor.elasticsearchclient.repository;

import io.simpolor.elasticsearchclient.repository.entity.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends ElasticsearchRepository<Student, String>, StudentRepositoryCustom {


}
