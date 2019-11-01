package io.simpolor.redis.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("Student")
public class Student implements Serializable {

	@Id
	private String key;

	private String name;

	private int grade;

	private int age;

	private List<String> hobby;

}
