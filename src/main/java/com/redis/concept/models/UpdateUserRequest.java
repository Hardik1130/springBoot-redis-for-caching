package com.redis.concept.models;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String name;
    private String email;
}
