package io.simpolor.elasticsearch2client.repository.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.simpolor.elasticsearch2client.repository.entity.Student;
import org.elasticsearch.ElasticsearchException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Function;

@Component
public class StudentJsonConverter {

    private final ObjectMapper objectMapper =
            new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .registerModule(new JavaTimeModule());

    public final Function<Student, String> toJson = student -> {
        try {
            return objectMapper.writeValueAsString(student);
        } catch (JsonProcessingException e) {
            throw new ElasticsearchException(e.getMessage(), e);
        }
    };

    public final Function<String, Student> toStudent = json -> {
        try {
            return objectMapper.readValue(json, Student.class);
        } catch (IOException e) {
            throw new ElasticsearchException(e.getMessage(), e);
        }
    };
}
