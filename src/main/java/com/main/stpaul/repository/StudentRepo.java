package com.main.stpaul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.main.stpaul.entities.Student;

public interface StudentRepo extends JpaRepository<Student,String>{
    
    List<Student> findAll();

    // @Query("SELECT s FROM Student s WHERE ")
    // Optional<Student> findById(long id);
    
}
