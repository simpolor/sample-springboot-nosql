package io.simpolor.elasticsearchclient.controller;

import io.simpolor.elasticsearchclient.model.ResultDto;
import io.simpolor.elasticsearchclient.model.SearchAfterDto;
import io.simpolor.elasticsearchclient.model.StudentDto;
import io.simpolor.elasticsearchclient.repository.SearchAfter;
import io.simpolor.elasticsearchclient.repository.entity.Student;
import io.simpolor.elasticsearchclient.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

	@RequestMapping("/search-after")
	public SearchAfterDto<StudentDto.StudentResponse> searchAfter(@RequestParam(required = false, defaultValue = "") String searchAfter,
														@RequestParam(required = false, defaultValue = "10") Integer size) {

		SearchAfter<Student> result = studentService.getAll(searchAfter, size);
		List<StudentDto.StudentResponse> responses =
				Optional.of(result.getResults())
						.orElse(Collections.emptyList())
						.stream()
						.map(StudentDto.StudentResponse::of)
						.collect(Collectors.toList());


		return new SearchAfterDto<>(responses, result.getNextSearchAfter(), result.getTotalCount());
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

	@RequestMapping(value="/{id}", method= RequestMethod.DELETE)
	public void delete(@PathVariable String id) {

		studentService.delete(id);
	}


}
