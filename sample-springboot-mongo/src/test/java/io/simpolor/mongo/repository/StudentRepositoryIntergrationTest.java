package io.simpolor.mongo.repository;

import io.simpolor.mongo.MongoApplication;
import io.simpolor.mongo.domain.Student;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MongoApplication.class)
public class StudentRepositoryIntergrationTest {

    @Autowired
    private StudentRepository studentRepository;

    @Before
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
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result);
        System.out.println("result : "+result);
    }
}
