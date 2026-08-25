package com.redis.concept.services;

import com.redis.concept.dao.UserRepository;
import com.redis.concept.models.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
//@RequiredArgsConstructor
public class UserService {

    private final RedisService redisService;
    private final UserRepository userRepository;

    public UserService(RedisService redisService, UserRepository userRepository) {
        this.redisService = redisService;
        this.userRepository = userRepository;
        redisService.setExpiryHandler(()-> Pair.of(true,Duration.ofMinutes(30)));
    }

    @PostConstruct
    public void testing()
    {
        new Thread(()->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            redisService.expireHash("45",Duration.ofSeconds(30));
            System.out.println("Expired go to Hash");
        }).start();
    }

    //    @CachePut(value = "USERS_DATA", key = "#result.id")
    public User createUser(CreateUserRequest userRequest) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setCityId(userRequest.getCityId());
        user.setCityName(userRequest.getCityName());
        redisService.cacheData(user.getCityId(), user.getId(), user,null);
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
        Page<User> userData = userRepository.searchUsersByNameAndEmail(
                userSearchRequest.getName(),
                userSearchRequest.getEmail(),
                pageable);
        return new UserResponse(userData.toList(), userData.getTotalElements());
    }


//    @CachePut(value = "USERS_DATA", key = "#id")
    public User updateUser(String id, UpdateUserRequest updateUserRequest) {
        User user = getUserById(id);
        if (!ObjectUtils.isEmpty(updateUserRequest.getName())) {
            user.setName(updateUserRequest.getName());
        }
        if (!ObjectUtils.isEmpty(updateUserRequest.getEmail())) {
            user.setEmail(updateUserRequest.getEmail());
        }
        redisService.cacheData(user.getCityId(), user.getId(), user,null);
        return userRepository.save(user);
    }

//    @CacheEvict(value = "USERS_DATA", key = "#id")
    public void deleteUser(String id) {
        User user = getUserById(id);
        userRepository.deleteById(id);
        redisService.evictDataFromHash(user.getCityId(), user.getId());
    }

//    @Cacheable(value = "USERS_DATA", key="#id")
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found With Given Id : ".concat(id)));
    }

    public User getById(String id,String cityId) {
        return Optional
                .ofNullable(redisService.getCachedData(cityId,id,User.class))
                .orElseGet(()->userRepository.findByIdAndCityId(id, cityId)
                            .map(user->{
                                    redisService.cacheData(user.getCityId(), user.getId(), user,null);
                                    return user;
                            })
                            .orElseThrow(() -> new RuntimeException("User Not Found With Given Id : ".concat(id))));
    }

    public String login(String email, String password, HttpSession session) {

        return userRepository.findByEmailIgnoreCase(email)
                .map(user -> {

                    if (!user.getPassword().equals(password)) {
                        return "Invalid Credentials";
                    }

                    session.setAttribute("user", user);
                    session.setAttribute("email", user.getEmail());
                    session.setAttribute("cityName", user.getCityName());
                    session.setAttribute("userId", user.getId());
                    return "Logged SuccessFull : ".concat(session.getId());

                })
                .orElse("Invalid Credentials");
    }


    public String getProfile(HttpSession session) {
        if(session==null)
        {
            return "Not LoggedIn";
        }
        Object email = session.getAttribute("email");
        Object role = session.getAttribute("role");
        return "Welcome : "+email+" With Role : "+role;
    }
}
