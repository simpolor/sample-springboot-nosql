package io.simpolor.mongodb.controller;

import io.simpolor.mongodb.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {StudentController.class}) // 특정 컨트롤러만 로드
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    public void testStudentTotalCount() throws Exception {

        when(studentService.getStudentTotalCount()).thenReturn(3L);

        this.mockMvc.perform(get("/student/totalcount"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

    }
}
