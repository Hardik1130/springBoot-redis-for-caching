package com.redis.concept.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class RedisListService {

    private final RedisTemplate<String, String> redisTemplate;

//    @PostConstruct
    public void demo() {

        String key = "my:list";
        String key2 = "my:list2";

        System.out.println("--------- LPUSH Demo ---------");
        lpush(key, "A");
        lpushAll(key, List.of("B","C")); // List = C B A

        System.out.println("After LPUSH:");
        lRange(key, 0, -1);

        System.out.println("--------- RPUSH Demo ---------");
        rpush(key, "D");
        rpush(key, "E"); // List = C B A D E

        System.out.println("After RPUSH:");
        lRange(key, 0, -1);

        System.out.println("--------- LPOP Demo ---------");
        lpop(key); // removes C

        System.out.println("After LPOP:");
        lRange(key, 0, -1);

        System.out.println("--------- RPOP Demo ---------");
        rpop(key); // removes E

        System.out.println("After RPOP:");
        lRange(key, 0, -1);

        System.out.println("--------- LTRIM Demo ---------");
        lTrim(key, 1, 2); // Keep B, A, D → trimmed output depends on current list

        System.out.println("After LTRIM:");
        lRange(key, 0, -1);

        System.out.println("--------- LLEN Demo ---------");
        llen(key);

        System.out.println("--------- List MOVE Demo ---------");

        // Prepare second list
        rpush(key2, "X");
        rpush(key2, "Y");

        System.out.println("Before MOVE:");
        System.out.println(key + ":");
        lRange(key, 0, -1);

        System.out.println(key2 + ":");
        lRange(key2, 0, -1);

        // Move: from list1 - LEFT to list2 - RIGHT
        lMove(
                key,
                RedisListCommands.Direction.LEFT,
                key2,
                RedisListCommands.Direction.RIGHT
        );

        System.out.println("After MOVE:");
        System.out.println(key + ":");
        lRange(key, 0, -1);

        System.out.println(key2 + ":");
        lRange(key2, 0, -1);
    }


    public void lpush(String key, String value) {
        listOps().leftPush(key, value);
    }

    public void lpushAll(String key, Collection<String> value) {
        listOps().leftPushAll(key, value);
    }

    public void rpush(String key, String value) {
        listOps().rightPush(key, value);
    }

    public void rpushAll(String key, Collection<String> value) {
        listOps().rightPushAll(key, value);
    }

    public void lpop(String key) {
        listOps().leftPop(key);
    }

    public void rpop(String key) {
        listOps().rightPop(key);
    }

    public void llen(String key) {
        System.out.println("LLEN: " + listOps().size(key));
    }

    public void lRange(String key, long start, long end) {
        System.out.println("LRANGE (" + key + "):");
        Objects.requireNonNullElse(listOps().range(key, start, end), emptyList())
                .forEach(System.out::println);
    }

    public void lTrim(String key, long start, long end) {
        listOps().trim(key, start, end);
    }

    public void lMove(String sourceKey,
                      RedisListCommands.Direction sourceDirection,
                      String targetKey,
                      RedisListCommands.Direction targetDirection) {

        listOps().move(sourceKey, sourceDirection, targetKey, targetDirection);
    }

    private ListOperations<String, String> listOps() {
        return redisTemplate.opsForList();
    }
}