package io.simpolor.mongodb2client.repository;

import com.mongodb.client.result.UpdateResult;
import io.simpolor.mongodb2client.domain.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentRepository {

    private final MongoTemplate mongoTemplate;

    public long totalCount(){
        return mongoTemplate.count(new Query(), Student.class);
    }

    public List<Student> findAll(){
        return mongoTemplate.find(new Query(), Student.class);
    }

    public Optional<Student> findById(String studentId){

        Query query = new Query(Criteria.where("_id").is(studentId));

        Student student = mongoTemplate.findOne(query, Student.class);

        return Optional.ofNullable(student);
    }

    public List<Student> findAllById(List<String> studentIds){

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(studentIds));

        return mongoTemplate.find(query, Student.class);
    }

    public Student insert(Student student){

        return mongoTemplate.insert(student);
    }

    public boolean updateByName(String studentId, String name){

        Query query = new Query(Criteria.where("_id").is(studentId));

        Update update = new Update();
        update.set("name", name);

        UpdateResult result = mongoTemplate.upsert(query, update, Student.class);

        return result.getModifiedCount() > 0;
    }

    public Student save(Student student){
        return mongoTemplate.save(student);
    }

    public void deleteById(String studentId){

        Query query = new Query(Criteria.where("_id").is(studentId));

        mongoTemplate.remove(query, Student.class);
    }
}
