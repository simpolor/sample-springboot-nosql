package io.simpolor.elasticsearch2client.repository.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

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

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private LocalDateTime createdAt;
}
