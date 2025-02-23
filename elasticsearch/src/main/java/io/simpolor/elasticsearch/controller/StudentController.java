package io.simpolor.elasticsearch.controller;

import io.simpolor.elasticsearch.model.ResultDto;
import io.simpolor.elasticsearch.model.StudentDto;
import io.simpolor.elasticsearch.repository.entity.Student;
import io.simpolor.elasticsearch.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

	private final StudentService studentService;

	@RequestMapping
	public List<StudentDto.StudentResponse> list() {

		List<Student> students = studentService.getAll();
		if(CollectionUtils.isEmpty(students)){
			return Collections.EMPTY_LIST;
		}

		return StudentDto.StudentResponse.of(students);
	}

	@RequestMapping(value="/{id}", method= RequestMethod.GET)
	public StudentDto.StudentResponse detail(@PathVariable String id) {

		Student student = studentService.get(id);

		return StudentDto.StudentResponse.of(student);
	}

	@RequestMapping(value="", method= RequestMethod.POST)
	public ResultDto register(@RequestBody StudentDto.StudentRequest request) {

		Student student =  studentService.create(request.toEntity());
		return ResultDto.of(student.getId());
	}

	@RequestMapping(value="/{id}", method= RequestMethod.PUT)
	public void modify(@PathVariable String id,
					   @RequestBody Student student) {

		student.setId(id);
		studentService.update(student);
	}

	@RequestMapping(value="/{id}/update", method= RequestMethod.PUT)
	public void modify2(@PathVariable String id,
						@RequestBody Student student) {

		student.setId(id);
		studentService.update2(student);
	}

	@RequestMapping(value="/{id}", method= RequestMethod.DELETE)
	public void delete(@PathVariable String id) {

		studentService.delete(id);
	}


}
