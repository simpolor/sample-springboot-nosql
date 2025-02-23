package io.simpolor.redis2.controller;

import io.simpolor.redis2.domain.Student;
import io.simpolor.redis2.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/student")
@RestController
public class StudentController {

	@Autowired
	private StudentService studentService;

	@RequestMapping(value="/totalcount", method= RequestMethod.GET)
	public long studentTotalCount() {
		return studentService.totalCount();
	}

	@RequestMapping(value="/list", method= RequestMethod.GET)
	public List<Student> studentList() {
		return studentService.getList();
	}

	@RequestMapping(value="/{key}", method= RequestMethod.GET)
	public Student studentView(@PathVariable String key) {
		return studentService.get(key);
	}

	@RequestMapping(value="", method= RequestMethod.POST)
	public Student studentRegister(@RequestBody Student student) {
		return studentService.insert(student);
	}

	@RequestMapping(value="/{key}", method= RequestMethod.PUT)
	public Student studentModify(@PathVariable String key,
                                 @RequestBody Student student) {
		student.setKey(key);
		return studentService.modify(student);
	}

	@RequestMapping(value="/{key}", method= RequestMethod.DELETE)
	public String studentDelete(@PathVariable String key) {
		return studentService.delete(key);
	}


}
