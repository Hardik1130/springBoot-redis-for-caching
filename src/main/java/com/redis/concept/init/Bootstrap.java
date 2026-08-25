package com.redis.concept.init;

import com.redis.concept.dao.UserRepository;
import com.redis.concept.models.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Bootstrap {

    private final UserRepository userRepository;

//    @PostConstruct
//    public void saveUser()
//    {
//        if (userRepository.count()==0) {
//            List<User> users = List.of(
//                    new User("1", "Alex", "alex@gmail.com","alex11","user"),
//                    new User("2", "john", "john@gmail.com","john11","admin")
//            );
//            userRepository.saveAll(users);
//        }
//    }

}
