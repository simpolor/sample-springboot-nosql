package io.simpolor.mongo.repository;

import com.mongodb.client.result.UpdateResult;
import io.simpolor.mongo.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MgStudentRepository {

    @Autowired
    private MongoTemplate mongoTemplate;


    public long count(){
        return mongoTemplate.count(new Query(), Student.class);
    }

    public List<Student> findAll(){
        return mongoTemplate.find(new Query(), Student.class);
    }

    public Optional<Student> findById(String id){
        return Optional.of(mongoTemplate.findOne(query(id), Student.class));
    }

    public List<Student> findIn(List<String> ids){
        return mongoTemplate.find(queryIn(ids), Student.class);
    }

    public boolean insert(String id, String name, int grade, int age, List<String> hobby){
        mongoTemplate.insert(new Student(id, name, grade, age, hobby));
        return true;
    }

    public boolean updateByName(String id, String name){
        UpdateResult result = mongoTemplate.upsert(query(id), update(name), Student.class);
        return (result.getModifiedCount() > 0) ? true : false;
    }

    public Student save(Student student){
        return mongoTemplate.save(student);
    }

    public void deleteById(String id){
        mongoTemplate.remove(query(id), Student.class);
    }

    private Query query(String id){
        return new Query(Criteria.where("_id").is(id));
    }

    private Query queryIn(List<String> ids){

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(ids));

        return query;
    }

    private Update update(String name){

        Update update = new Update();
        update.set("name", name);

        return update;
    }

}
