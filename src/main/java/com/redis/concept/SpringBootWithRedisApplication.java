package com.redis.concept;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableCaching
@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30)
public class SpringBootWithRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWithRedisApplication.class, args);
	}

}