package com.redis.concept.controller;


import com.redis.concept.models.*;
import com.redis.concept.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session)
    {
           return userService.login(email, password, session);
    }

    @GetMapping("/profile")
    public String getProfile(HttpServletRequest servletRequest)
    {
        return userService.getProfile(servletRequest.getSession(false));
    }

    @GetMapping("/logout")
    public void logout(HttpSession session)
    {
          session.invalidate();
    }



    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest userRequest) {
        return ResponseEntity.ok().body(userService.createUser(userRequest));
    }

    @GetMapping
    public ResponseEntity<UserResponse> getAllUsers(UserSearchRequest userSearchRequest) {
        return ResponseEntity.ok().body(userService.getAllUsers(userSearchRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @GetMapping("/{id}/{cityId}")
    public ResponseEntity<User> getUserById(@PathVariable String id,@PathVariable String cityId) {
        return ResponseEntity.ok().body(userService.getById(id,cityId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UpdateUserRequest userRequest) {
        userService.updateUser(id, userRequest);
        return ResponseEntity.ok().body("User Update Successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body("User Deleted Successfully");
    }

}
