package com.redis.concept.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSortedSETService {

    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void run() {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

        System.out.println("========== REDIS SORTED SET DEMO START ==========\n");

        zSetOps.add("leaderboard", "alice", 1500);
        zSetOps.add("leaderboard", "bob", 1350);
        zSetOps.add("leaderboard", "charlie", 1700);
        zSetOps.add("leaderboard", "yakuza", 1650);
        zSetOps.add("leaderboard", "david", 1600);
        zSetOps.add("leaderboard", "eva", 1450);

        System.out.println(" <- leaderboard Data Added → ");

        System.out.println("\n ZRANGE leaderboard ASC:");
        System.out.println(zSetOps.range("leaderboard", 0, -1));

        System.out.println("\nZRANGE WITH SCORES:");
        for (ZSetOperations.TypedTuple<String> leaderboard : zSetOps.rangeWithScores("leaderboard", 0, -1)) {
            System.out.println(leaderboard);
        }

        System.out.println("\n ZREVRANGE Top 3:");
        for (ZSetOperations.TypedTuple<String> score : zSetOps.reverseRangeWithScores("leaderboard", 0, 2)) {
            System.out.println(score);
        }

        System.out.println("\n ZRANGEBYSCORE 1400 - 1600:");
        for (ZSetOperations.TypedTuple<String> score : zSetOps.rangeByScoreWithScores("leaderboard", 1400, 1600)) {
            System.out.println(score);
        }

        zSetOps.add("lexset", "aaaa", 0);
        zSetOps.add("lexset", "alpha", 0);
        zSetOps.add("lexset", "b", 0);
        zSetOps.add("lexset", "c", 0);
        zSetOps.add("lexset", "foo", 0);
        zSetOps.add("lexset", "zap", 0);
        zSetOps.add("lexset", "zip", 0);

        System.out.println("\n ZRANGEBYLEX:");
        for (String s : zSetOps.rangeByLex("lexset", Range.closed("a", "m"))) {
            System.out.println(s);
        }

        System.out.println("\n ZSCORE alice → " + zSetOps.score("leaderboard", "alice"));

        System.out.println("\n ZRANK ASC alice → " + zSetOps.rank("leaderboard", "alice"));

        System.out.println("ZREVRANK DESC alice → " + zSetOps.reverseRank("leaderboard", "alice"));

        System.out.println("\n ZCOUNT 1500 - 2000:");
        System.out.println(zSetOps.count("leaderboard", 1500, 2000));

        System.out.println("\n ZLEXCOUNT lexset [a - [e:");
        System.out.println(zSetOps.lexCount("lexset", Range.closed("a", "e")));


        zSetOps.add("leaderboard", "alice", 1800);
        System.out.println("\n Update score of alice → " + zSetOps.score("leaderboard", "alice"));

        zSetOps.incrementScore("leaderboard", "david", 50);
        System.out.println("\n Increment david → " + zSetOps.score("leaderboard", "david"));


        zSetOps.add("delset", "alice", 1500);
        zSetOps.add("delset", "bob", 1300);
        zSetOps.add("delset", "carl", 1400);
        zSetOps.add("delset", "david", 1600);
        zSetOps.add("delset", "eric", 1200);

        zSetOps.remove("delset", "bob");
        System.out.println("\n After ZREM bob → " + zSetOps.rangeWithScores("delset", 0, -1));

        zSetOps.removeRange("delset", 0, 1);
        System.out.println("\n Remove by rank 0-1 → " + zSetOps.rangeWithScores("delset", 0, -1));

        zSetOps.removeRangeByScore("delset", Double.NEGATIVE_INFINITY, 1300);
        System.out.println("\n Remove score <1300 → " + zSetOps.rangeWithScores("delset", 0, -1));


        zSetOps.add("monthly_lb", "ankit", 100);
        zSetOps.add("monthly_lb", "rahul", 150);
        zSetOps.add("monthly_lb", "sneha", 90);

        zSetOps.add("weekly_lb", "ankit", 50);
        zSetOps.add("weekly_lb", "rahul", 200);
        zSetOps.add("weekly_lb", "john", 80);

        zSetOps.unionAndStore("monthly_lb", "weekly_lb", "union_lb");
        System.out.println("\n UNION STORE → " + zSetOps.rangeWithScores("union_lb", 0, -1));

        zSetOps.intersectAndStore("monthly_lb", "weekly_lb", "inter_lb");
        System.out.println("\n INTERSECTION STORE → " + zSetOps.rangeWithScores("inter_lb", 0, -1));

        System.out.println("\n ZPOPMAX leaderboard → " + zSetOps.popMax("leaderboard"));

        System.out.println("ZPOPMAX 2 leaderboard → " + zSetOps.popMax("leaderboard", 2));

        zSetOps.add("racer_users2", "ankii", 10);
        zSetOps.add("racer_users2", "bhuvv", 18);
        zSetOps.add("racer_users2", "shubham", 15);

        System.out.println("\n ZPOPMIN → " + zSetOps.popMin("racer_users2")); //
        System.out.println("ZPOPMIN 2 → " + zSetOps.popMin("racer_users2", 2));

        System.out.println("\n ZSCAN final_lb 0 MATCH *s* COUNT 5:");
        zSetOps.add("final_lb", "boss", 100);
        zSetOps.add("final_lb", "ross", 200);
        zSetOps.add("final_lb", "messi", 300);
        zSetOps.add("final_lb", "sara", 150);
        zSetOps.add("final_lb", "jesse", 175);

        Cursor<ZSetOperations.TypedTuple<String>> cursor = zSetOps.scan("final_lb", ScanOptions.scanOptions().match("*i*").count(5).build());
        cursor.forEachRemaining(System.out::println);

        System.out.println("\n========== REDIS SORTED SET DEMO END ==========");
    }

}