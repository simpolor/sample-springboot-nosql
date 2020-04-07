package io.simpolor.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Data
@Document(indexName = "student", type = "doc")
public class Student {


	// @JsonInclude(JsonInclude.Include.NON_NULL)
	// @JsonIgnore
	// @Field(type = FieldType.Keyword, store = true)

	@Id
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String id;

	private int seq;

	private String name;

	private int grade;

	private int age;

	private List<String> hobby;

	public Student(){ }

	public Student(String id, int seq, String name, int grade, int age, List<String> hobby) {
		this.id = id;
		this.seq = seq;
		this.name = name;
		this.grade = grade;
		this.age = age;
		this.hobby = hobby;
	}

}
