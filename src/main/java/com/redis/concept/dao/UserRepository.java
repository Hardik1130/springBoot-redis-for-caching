package com.redis.concept.dao;


import com.redis.concept.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "select * from users where name ILIKE CONCAT('%', :name, '%') and email ILIKE CONCAT('%', :email, '%')",
            countQuery = "select count(*) from users where name ILIKE CONCAT('%', :name, '%') and email ILIKE CONCAT('%', :email, '%')"
            , nativeQuery = true)
    Page<User> searchUsersByNameAndEmail(String name, String email, Pageable pageable);
}
