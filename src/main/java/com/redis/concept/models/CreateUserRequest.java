package com.redis.concept.models;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String name;
    private String email;
}
