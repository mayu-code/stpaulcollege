package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.StudentAcademics;

public interface StudentAcademicsRepo extends JpaRepository<StudentAcademics,String> {
    
}
