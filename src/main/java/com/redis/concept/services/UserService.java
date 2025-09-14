package com.redis.concept.services;

import com.redis.concept.dao.UserRepository;
import com.redis.concept.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RedisTemplate<String,String> redisTemplate;

    private final UserRepository userRepository;

    @CachePut(value = "USERS_DATA", key = "#result.id")
    public User createUser(CreateUserRequest userRequest) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        return userRepository.save(user);
    }

    public UserResponse getAllUsers(UserSearchRequest userSearchRequest) {
        Sort sort = Sort.by(userSearchRequest.getSortKey());
        if (userSearchRequest.getSortValue().equalsIgnoreCase("asc")) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(userSearchRequest.getPage(), userSearchRequest.getSize(), sort);
//        Page<User> userData = userRepository.findAll(pageable);
        Page<User> userData = userRepository.searchUsersByNameAndEmail(
                userSearchRequest.getName(),
                userSearchRequest.getEmail(),
                pageable);
        return new UserResponse(userData.toList(), userData.getTotalElements());
    }

    @CachePut(value = "USERS_DATA", key = "#id")
    public User updateUser(String id, UpdateUserRequest updateUserRequest) {
        User user = getUserById(id);
        if (!ObjectUtils.isEmpty(updateUserRequest.getName())) {
            user.setName(updateUserRequest.getName());
        }
        if (!ObjectUtils.isEmpty(updateUserRequest.getEmail())) {
            user.setEmail(updateUserRequest.getEmail());
        }
        return userRepository.save(user);
    }

    @CacheEvict(value = "USERS_DATA", key = "#id")
    public void deleteUser(String id) {
        getUserById(id);
        userRepository.deleteById(id);
    }

    @Cacheable(value = "USERS_DATA", key = "#id")
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found With Given Id : ".concat(id)));
    }

}
