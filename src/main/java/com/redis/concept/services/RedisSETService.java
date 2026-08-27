package com.redis.concept.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class RedisSETService {

    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void redisSETDataStructure() {

        System.out.println("-------------- SET DEMO STARTED --------------");

        redisTemplate.opsForSet().add("users", "alice");
        redisTemplate.opsForSet().add("users", "yakuza");
        redisTemplate.opsForSet().add("users", "bob");
        redisTemplate.opsForSet().add("users", "charlie");
        redisTemplate.opsForSet().add("users", "david");
        redisTemplate.opsForSet().add("users", "eva");

        System.out.println("Users added to SET: " + redisTemplate.opsForSet().members("users"));

        Set<String> users = redisTemplate.opsForSet().members("users");
        Long userCount = redisTemplate.opsForSet().size("users");

        System.out.println("SMEMBERS users → " + users);
        System.out.println("SCARD users → " + userCount);

        redisTemplate.opsForSet().remove("users", "charlie");
        redisTemplate.opsForSet().remove("users", "eva");

        System.out.println("After SREM (charlie, eva): " + redisTemplate.opsForSet().members("users"));

        Boolean isAlice = redisTemplate.opsForSet().isMember("users", "alice");
        Boolean isCharlie = redisTemplate.opsForSet().isMember("users", "charlie");

        System.out.println("SISMEMBER users alice → " + isAlice);
        System.out.println("SISMEMBER users charlie → " + isCharlie);

        redisTemplate.opsForSet().add("premium_users", "bob");
        redisTemplate.opsForSet().add("premium_users", "david");
        redisTemplate.opsForSet().add("premium_users", "frank");

        System.out.println("SMEMBERS users → " + redisTemplate.opsForSet().members("users"));
        System.out.println("SMEMBERS premium_users → " + redisTemplate.opsForSet().members("premium_users"));

        Set<String> union = redisTemplate.opsForSet().union("users", "premium_users");
        System.out.println("SUNION users premium_users → " + union);

        Set<String> intersection = redisTemplate.opsForSet().intersect("users", "premium_users");
        System.out.println("SINTER users premium_users → " + intersection);

        Set<String> difference = redisTemplate.opsForSet().difference("users", "premium_users");
        System.out.println("SDIFF → " + difference);

        String randomOne = redisTemplate.opsForSet().randomMember("users");
        System.out.println("SRANDMEMBER users → " + randomOne);

        Set<String> randomTwo = new HashSet<>(Objects.requireNonNullElse(redisTemplate
                .opsForSet().randomMembers("users", 2), emptyList()));
        System.out.println("SRANDMEMBER users 2 → " + randomTwo);

        String popped = redisTemplate.opsForSet().pop("users");
        System.out.println("SPOP users → " + popped);
        System.out.println("Users after SPOP → " + redisTemplate.opsForSet().members("users"));

        System.out.println("-------------- SET DEMO FINISHED --------------");
    }

}