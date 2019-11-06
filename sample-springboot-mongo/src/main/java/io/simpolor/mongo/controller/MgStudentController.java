package io.simpolor.mongo.controller;

import io.simpolor.mongo.domain.Student;
import io.simpolor.mongo.service.MgStudentService;
import io.simpolor.mongo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/mg/student")
@RestController
public class MgStudentController {

	@Autowired
	private MgStudentService mgStudentService;

	@RequestMapping(value="/totalcount", method= RequestMethod.GET)
	public long studentTotalCount() {
		return mgStudentService.getStudentTotalCount();
	}

	@RequestMapping(value="/list", method= RequestMethod.GET)
	public List<Student> studentList() {
		return mgStudentService.getStudentList();
	}

	@RequestMapping(value="/{id}", method= RequestMethod.GET)
	public Student studentView(@PathVariable String id) {
		return mgStudentService.getStudent(id);
	}

	@RequestMapping(value="", method= RequestMethod.POST)
	public Student studentRegister(@RequestBody Student student) {
		return mgStudentService.registerStudent(student);
	}

	@RequestMapping(value="/{id}", method= RequestMethod.PUT)
	public Student studentModify(@PathVariable String id,
                                 @RequestBody Student student) {
		student.setId(id);
		return mgStudentService.modifyStudent(student);
	}

	@RequestMapping(value="/{id}", method= RequestMethod.DELETE)
	public String studentDelete(@PathVariable String id) {
		return mgStudentService.deleteStudent(id);
	}


}
