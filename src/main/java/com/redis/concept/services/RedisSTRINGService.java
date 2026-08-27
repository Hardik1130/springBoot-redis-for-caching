package com.redis.concept.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisSTRINGService {

    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void run() {
        var ops = redisTemplate.opsForValue();

        System.out.println("\n========== REDIS STRING DEMO START ==========\n");


        // -------------------------------------------------------------
        //  CREATE (WRITE)
        // -------------------------------------------------------------
        System.out.println("---- CREATE OPERATIONS ----");

        // SET
        ops.set("user:name", "Alice");
        ops.set("app:mode", "production");

        // SET NX
        ops.setIfAbsent("user:email", "alice@gmail.com");

        // SET XX
        ops.setIfPresent("user:email", "updated@mail.com");

        // MSET (multiple writes)
        ops.multiSet(
                java.util.Map.of(
                        "user:city", "Delhi",
                        "user:role", "admin",
                        "user:age", "25"
                )
        );

        System.out.println("Created keys: user:name, app:mode, user:email, user:*");


        // -------------------------------------------------------------
        //  READ
        // -------------------------------------------------------------
        System.out.println("\n---- READ OPERATIONS ----");

        // GET
        System.out.println("GET user:name → " + ops.get("user:name"));

        // MGET
        List<String> readValues = ops.multiGet(List.of("user:name", "user:city", "user:role"));
        System.out.println("MGET → " + readValues);

        // STRLEN
        System.out.println("STRLEN user:name → " + ops.size("user:name"));

        // GETRANGE
        String substring = ops.get("user:name", 0, 3);
        System.out.println("GETRANGE user:name 0-3 → " + substring);


        // -------------------------------------------------------------
        //  UPDATE
        // -------------------------------------------------------------
        System.out.println("\n---- UPDATE OPERATIONS ----");

        // Overwrite with SET
        ops.set("user:name", "David Braga");

        // APPEND
        ops.append("user:name", " Johnson");

        // SETRANGE
        ops.set("user:name", "Mr", 0);

        // GETSET
        String oldToken = ops.getAndSet("session:token", "newToken123");
        System.out.println("Old session token (GETSET) → " + oldToken);


        // -------------------------------------------------------------
        //  DELETE
        // -------------------------------------------------------------
        System.out.println("\n---- DELETE OPERATIONS ----");

        // DEL
        redisTemplate.delete("user:name");

        // UNLINK (async delete)
        redisTemplate.unlink("session:token");


        // -------------------------------------------------------------
        // EXPIRE / TTL
        // -------------------------------------------------------------
        System.out.println("\n---- EXPIRE OPERATIONS ----");

        ops.set("session:token", "abc-token");
        redisTemplate.expire("session:token", Duration.ofSeconds(50));

        System.out.println("TTL session:token → " + redisTemplate.getExpire("session:token"));

        // PERSIST
        redisTemplate.persist("session:token");

        // SETEX
        ops.set("otp:mobile", "30", Duration.ofSeconds(10));

        // PSETEX
        ops.set("temp:key", "Hello", Duration.ofMillis(5000));


        // -------------------------------------------------------------
        // ADVANCED OPERATIONS
        // -------------------------------------------------------------
        System.out.println("\n---- ADVANCE OPERATIONS ----");

        // Counter setup
        ops.set("page:views", "0");

        // INCR
        ops.increment("page:views");

        // INCRBY
        ops.increment("page:views", 10);

        // DECR
        ops.decrement("page:views");

        // DECRBY
        ops.decrement("page:views", 6);

        // INCRBYFLOAT
        ops.increment("wallet:balance", 50.75);
        ops.increment("wallet:balance", 20.75);

        // COPY
        redisTemplate.copy("user:email", "user:email:backup", false);

        // SCAN equivalent (Pattern search)
        System.out.println("\nSCAN MATCH user:*");
        var cursor = redisTemplate.scan(
                ScanOptions.scanOptions()
                        .match("user:*")
                        .count(10)
                        .build());

        cursor.forEachRemaining(key -> System.out.println("→ " + key));


        System.out.println("\n========== REDIS STRING DEMO END ==========\n");
    }

}