package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.GuardianInfo;

public interface GuardianInfoRepo extends JpaRepository<GuardianInfo,String>{
    
}
