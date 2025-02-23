package io.simpolor.mongodb2.service;

import io.simpolor.mongodb2.domain.Student;
import io.simpolor.mongodb2.repository.StudentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetStudentTotalCount(){
        // given
        long reuturnValue = 3L;
        when(studentRepository.count()).thenReturn(reuturnValue);

        // when
        long result = studentService.getStudentTotalCount();


        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(3L, result);
    }

    @Test
    public void testGetStudentList(){

        // given
        List<Student> students = new ArrayList<>();
        students.add(new Student("5d90970355f7711b44d1dbd3", "단순색", 3, 19, Arrays.asList("축가")));
        students.add(new Student("5d90aa5055f77119b0175f5a", "가장", 2, 18, Arrays.asList("잠자기")));

        when(studentRepository.findAll()).thenReturn(students);


        // when
        List<Student> result = studentService.getStudentList();


        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    public void testGetStudent(){

    }

    public void testRegisterStudent(){

    }

    public void testrModifyStudent(){

    }

    public void testrDeleteStudent(){

    }
}
