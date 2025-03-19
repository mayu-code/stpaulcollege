package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.CollegeFees;

public interface CollegeFeesRepo extends JpaRepository<CollegeFees,Long>{
    
}
