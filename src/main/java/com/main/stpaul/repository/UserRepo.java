package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.User;

public interface UserRepo extends JpaRepository<User,String>{
    User findByEmail(String email);
}
