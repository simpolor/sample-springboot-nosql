package io.simpolor.elasticsearch.repository;

import io.simpolor.elasticsearch.domain.Student;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class CustomRepositoryImpl implements CustomRepository {

    @Autowired
    private Client client;

    @Override
    public Student update(Student student) {

        Map<String, Object> json = new HashMap<>();
        json.put("name", student.getName());
        json.put("grade",student.getGrade());
        json.put("age", student.getAge());
        json.put("hobby", student.getHobby());

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("student");
        updateRequest.type("doc");
        updateRequest.id(student.getId());
        updateRequest.doc(json);

        client.update(updateRequest).actionGet();

        return student;
    }


}
