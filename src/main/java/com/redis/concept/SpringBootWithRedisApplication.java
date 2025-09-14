package com.redis.concept;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringBootWithRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWithRedisApplication.class, args);
	}

}
