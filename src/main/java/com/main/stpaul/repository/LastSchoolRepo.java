package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.LastSchool;

public interface LastSchoolRepo extends JpaRepository<LastSchool,String> {
    
}
