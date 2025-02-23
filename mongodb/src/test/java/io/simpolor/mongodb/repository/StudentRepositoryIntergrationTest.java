package io.simpolor.mongodb.repository;

import io.simpolor.mongodb.MongodbApplication;
import io.simpolor.mongodb.domain.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest(classes = MongodbApplication.class)
@EnableAutoConfiguration(exclude={EmbeddedMongoAutoConfiguration.class})
public class StudentRepositoryIntergrationTest {

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    public void before() {

        studentRepository.deleteAll();

        Student student = new Student();
        student.setName("홍길동");
        student.setGrade(2);
        student.setAge(18);
        student.setHobby(Arrays.asList("축구","달리기"));

        studentRepository.save(student);
    }

    @Test
    public void testCount() {

        // when
        long result = studentRepository.count();

        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result);
        System.out.println("result : "+result);
    }
}
