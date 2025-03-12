package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.Student;

public interface StudentRepo extends JpaRepository<Student,String>{
    
}
