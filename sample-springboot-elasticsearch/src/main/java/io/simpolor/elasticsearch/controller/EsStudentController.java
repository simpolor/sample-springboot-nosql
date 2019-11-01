package io.simpolor.elasticsearch.controller;

import io.simpolor.elasticsearch.domain.Student;
import io.simpolor.elasticsearch.service.EsStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/es/student")
@RestController
public class EsStudentController {

	@Autowired
	private EsStudentService esStudentService;

	@RequestMapping(value="/totalcount", method= RequestMethod.GET)
	public long studentTotalCount() {
		return esStudentService.getStudentTotalCount();
	}

	@RequestMapping(value="/list", method= RequestMethod.GET)
	public List<Student> studentList() {
		return esStudentService.getStudentList();
	}

	@RequestMapping(value="/{id}", method= RequestMethod.GET)
	public Student studentView(@PathVariable String id) {
		return esStudentService.getStudent(id);
	}

	@RequestMapping(value="", method= RequestMethod.POST)
	public Student studentRegister(@RequestBody Student student) {
		return esStudentService.registerStudent(student);
	}

	@RequestMapping(value="/{id}", method= RequestMethod.PUT)
	public Student studentModify(@PathVariable String id,
							 @RequestBody Student student) {
		student.setId(id);
		return esStudentService.modifyStudent(student);
	}

	@RequestMapping(value="/{seq}", method= RequestMethod.DELETE)
	public String studentDelete(@PathVariable String id) {
		return esStudentService.deleteStudent(id);

	}


}
