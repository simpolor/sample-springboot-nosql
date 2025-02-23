package io.simpolor.elasticsearch.model;

import io.simpolor.elasticsearch.repository.entity.Student;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class StudentDto {

	@Getter
	@Setter
	public static class StudentRequest {

		private String id;
		private String name;
		private Integer grade;
		private Integer age;

		public Student toEntity(){

			Student student = new Student();
			student.setId(this.id);
			student.setName(this.name);
			student.setGrade(this.grade);
			student.setAge(this.age);

			return student;
		}
	}

	@Getter
	@Setter
	public static class StudentResponse {

		private String id;
		private String name;
		private Integer grade;
		private Integer age;

		public static StudentResponse of(Student student){

			StudentResponse response = new StudentResponse();
			response.setId(student.getId());
			response.setName(student.getName());
			response.setGrade(student.getGrade());
			response.setAge(student.getAge());

			return response;
		}

		public static List<StudentResponse> of(List<Student> students){

			return students.stream()
					.map(StudentResponse::of)
					.collect(Collectors.toList());
		}
	}




}
