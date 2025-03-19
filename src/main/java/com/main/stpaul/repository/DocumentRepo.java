package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.Documents;

public interface DocumentRepo extends JpaRepository<Documents,Long>{
    
}
