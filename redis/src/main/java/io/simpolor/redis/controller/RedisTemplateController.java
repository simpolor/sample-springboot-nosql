package io.simpolor.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/redis/template")
@RestController
public class RedisTemplateController {

	private final String prefix = "SIMPOLOR:";

	private int expireSeconds = 3600;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * private ValueOperations<String, String> valueOperations; // String
	 * private SetOperations<String, String> setOperations; // Set
	 * private ZSetOperations<String, String> zSetOperations; // Sorted Set
	 * private HashOperations<String, String, String> hashOperations; // HashSet
	 * private ListOperations<String, String> listOperations; // List
	 */

	@RequestMapping(value="", method= RequestMethod.POST)
	public String redisSet(@RequestBody Map<String, Object> params){

		redisTemplate.opsForValue().set(prefix+params.get("key"), params.get("value").toString());
		// redisTemplate.opsForValue().set(prefix+key, value, expireSeconds);

		return "OK";
	}

	@RequestMapping(value="/{key}", method= RequestMethod.GET)
	public String redisGet(@PathVariable String key){
		return redisTemplate.opsForValue().get(prefix+key);
	}

	@RequestMapping(value="/{key}", method= RequestMethod.DELETE)
	public String redisDel(@PathVariable String key){
		redisTemplate.delete(prefix+key);
		return key;
	}
}
