package io.simpolor.elasticsearch.repository.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "student")
@Getter
@Setter
public class Student {

	@Id
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String id;

	private String name;
	private Integer grade;
	private Integer age;
}
