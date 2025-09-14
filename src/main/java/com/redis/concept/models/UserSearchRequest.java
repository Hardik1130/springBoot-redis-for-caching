package com.redis.concept.models;

import lombok.Data;
import org.springframework.util.ObjectUtils;

@Data
public class UserSearchRequest {

    private int page;
    private int size;

    private String sortKey;
    private String sortValue;

    private String name;
    private String email;

    public String getName() {
        return ObjectUtils.isEmpty(this.name) ? "" : this.name;
    }

    public String getEmail() {
        return ObjectUtils.isEmpty(this.email) ? "" : this.email;
    }
}
