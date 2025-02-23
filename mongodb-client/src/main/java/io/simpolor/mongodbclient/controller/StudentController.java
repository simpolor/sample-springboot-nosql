package io.simpolor.mongodbclient.controller;

import io.simpolor.mongodbclient.domain.Student;
import io.simpolor.mongodbclient.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

	private final StudentService studentService;

	@RequestMapping(value="/totalcount", method= RequestMethod.GET)
	public long totalCount() {
		return studentService.totalCount();
	}

	@RequestMapping(value="/list", method= RequestMethod.GET)
	public List<Student> list() {
		return studentService.getList();
	}

	@RequestMapping(value="/{studentId}", method= RequestMethod.GET)
	public Student view(@PathVariable String studentId) {

		return studentService.get(studentId);
	}

	@RequestMapping(method= RequestMethod.POST)
	public Student register(@RequestBody Student student) {

		return studentService.insert(student);
	}

	@RequestMapping(value="/{studentId}", method= RequestMethod.PUT)
	public Student modify(@PathVariable String studentId,
						  @RequestBody Student student) {

		student.setId(studentId);

		return studentService.update(student);
	}

	@RequestMapping(value="/{studentId}", method= RequestMethod.DELETE)
	public String delete(@PathVariable String studentId) {

		return studentService.delete(studentId);
	}


}
