package com.redis.concept.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    @Setter
    private Supplier<Pair<Boolean, Duration>> expiryHandler;

    public <T> void cacheData(final String key, final String hashkey,
                              final T data, final Duration duration) {


        redisTemplate.opsForHash().put(key, hashkey, serializeData(data));
        handleHashKeysExpiry(key, List.of(hashkey), duration);
    }

    public <R> R getCachedData(final String key, final String hashkey,
                               final Class<R> clazz) {
        return Optional
                .ofNullable(redisTemplate.opsForHash().get(key, hashkey))
                .map(data -> deserializeData(data, clazz))
                .orElse(null);
    }

    public void evictDataFromHash(final String key, final String hashkey) {
        redisTemplate.opsForHash().delete(key, hashkey);
    }

    public void handleHashKeysExpiry(final String key, final Collection<Object> hashKeys,
                                     final Duration duration) {


        if (Objects.nonNull(duration)) {
            expireHashKeys(key, hashKeys, duration);
        } else if (Objects.nonNull(expiryHandler) && expiryHandler.get().getFirst()) {
            expireHashKeys(key, hashKeys, expiryHandler.get().getSecond());
        }
    }

    public void handleHashExpiry(final String key, final Duration duration) {
        if (Objects.nonNull(duration)) {
            expireHash(key, duration);
        } else if (Objects.nonNull(expiryHandler) && expiryHandler.get().getFirst()) {
            expireHash(key, expiryHandler.get().getSecond());
        }
    }

    public void expireHashKeys(final String key,
                               final Collection<Object> hashKeys,
                               final Duration duration) {


        redisTemplate
                .opsForHash()
                .expire(key, duration, hashKeys);
    }

    public void expireHash(final String key,
                           final Duration duration) {


        redisTemplate.expire(key, duration);
    }

    private <T> String serializeData(final T input) {
        try {
            return objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while serializing cached data");
        }
    }

    private <R> R deserializeData(final Object data, final Class<R> clazz) {
        try {
            return objectMapper.readValue(data.toString(), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while deserializing cached data");
        }
    }


}