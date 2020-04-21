package io.simpolor.elasticsearch.controller;

import io.simpolor.elasticsearch.domain.SearchAfter;
import io.simpolor.elasticsearch.domain.Student;
import io.simpolor.elasticsearch.service.SearchAfterStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/after/student")
@RestController
public class SearchAfterStudentController {

	@Autowired
	private SearchAfterStudentService afterStudentService;

	@RequestMapping(value="/totalcount", method= RequestMethod.GET)
	public long studentTotalCount() {
		return afterStudentService.getStudentTotalCount();
	}

	@RequestMapping(value="/list", method= RequestMethod.GET)
	public SearchAfter<Student> studentList(
			@RequestParam(defaultValue = "", required = false, name="search_after") String searchAfter,
			@RequestParam(defaultValue = "10", required = false, name="size") int size
	) {
		return afterStudentService.getStudentList(searchAfter, size);
	}

	@RequestMapping(value="/{id}", method= RequestMethod.GET)
	public Student studentView(@PathVariable String id) {
		return afterStudentService.getStudent(id);
	}

	@RequestMapping(value="", method= RequestMethod.POST)
	public Student studentRegister(@RequestBody Student student) {

		return afterStudentService.registerStudent(student);
	}

	@RequestMapping(value="/{id}", method= RequestMethod.PUT)
	public Student studentModify(@PathVariable String id,
							 @RequestBody Student student) {
		student.setId(id);
		return afterStudentService.modifyStudent(student);
	}

	@RequestMapping(value="/{seq}", method= RequestMethod.DELETE)
	public String studentDelete(@PathVariable String id) {
		return afterStudentService.deleteStudent(id);

	}


}
