package io.simpolor.mongodb2.controller;

import io.simpolor.mongodb2.domain.Student;
import io.simpolor.mongodb2.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

	private final StudentService studentService;

	@RequestMapping(value="/totalcount", method= RequestMethod.GET)
	public long studentTotalCount() {
		return studentService.getStudentTotalCount();
	}

	@RequestMapping(value="/list", method= RequestMethod.GET)
	public List<Student> studentList() {
		return studentService.getStudentList();
	}

	@RequestMapping(value="/{id}", method= RequestMethod.GET)
	public Student studentView(@PathVariable String id) {
		return studentService.getStudent(id);
	}

	@RequestMapping(value="", method= RequestMethod.POST)
	public Student studentRegister(@RequestBody Student student) {
		return studentService.registerStudent(student);
	}

	@RequestMapping(value="/{id}", method= RequestMethod.PUT)
	public Student studentModify(@PathVariable String id,
                                 @RequestBody Student student) {
		student.setId(id);
		return studentService.modifyStudent(student);
	}

	@RequestMapping(value="/{id}", method= RequestMethod.DELETE)
	public String studentDelete(@PathVariable String id) {
		return studentService.deleteStudent(id);
	}


}
