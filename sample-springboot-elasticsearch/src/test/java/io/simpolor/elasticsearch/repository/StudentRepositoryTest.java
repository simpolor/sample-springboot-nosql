package io.simpolor.elasticsearch.repository;

import io.simpolor.elasticsearch.ElasticsearchApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class , webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void test(){
        System.out.println("studentRepository.count() : "+studentRepository.count());
    }
}
