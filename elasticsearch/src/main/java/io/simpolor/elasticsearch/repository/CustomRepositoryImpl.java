package io.simpolor.elasticsearch.repository;

import io.simpolor.elasticsearch.repository.entity.Student;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CustomRepositoryImpl implements CustomRepository {

    private final RestHighLevelClient client;

    @Override
    public Student update(Student student) {

        Map<String, Object> json = new HashMap<>();
        json.put("name", student.getName());
        json.put("grade",student.getGrade());
        json.put("age", student.getAge());

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("student");
        updateRequest.id(student.getId());
        updateRequest.doc(json);

        try {
            client.update(updateRequest, RequestOptions.DEFAULT).getGetResult();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }

        return student;
    }
}
