package com.main.stpaul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.main.stpaul.entities.LastSchool;

public interface LastSchoolRepo extends JpaRepository<LastSchool,String> {
    
    @Query("SELECT l FROM LastSchool l WHERE l.student.id=:id AND l.isDelete=false")
    Optional<LastSchool> findByStudent(long id);
    
    @Modifying
    @Query("UPDATE LastSchool l SET l.isDelete=true AND l.deleteDate=now WHERE l.lsId=:id")
    void deleteLastSchool(long id);
}
