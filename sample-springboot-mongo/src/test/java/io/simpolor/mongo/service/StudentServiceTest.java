package io.simpolor.mongo.service;

import io.simpolor.mongo.domain.Student;
import io.simpolor.mongo.repository.StudentRepository;
import org.junit.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.when;

public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Before
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
        Assert.assertNotNull(result);
        Assert.assertEquals(3L, result);
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
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
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
