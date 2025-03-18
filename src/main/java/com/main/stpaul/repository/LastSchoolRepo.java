package com.main.stpaul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.main.stpaul.dto.response.LastSchoolResponse;
import com.main.stpaul.entities.LastSchool;

public interface LastSchoolRepo extends JpaRepository<LastSchool,String> {
    
    @Query("SELECT new com.main.stpaul.dto.response.LastSchoolResponse(" +
           "l.lsId, l.collegeName, l.lastStudentId, l.rollNo, l.uid, " +
           "l.examination, l.examMonth, l.marksObtained, l.result) " +
           "FROM LastSchool l WHERE l.student.id = :id AND l.isDelete = false")
    Optional<LastSchoolResponse> findByStudent(String id);
    
    @Modifying
    @Query("UPDATE LastSchool l SET l.isDelete=true AND l.deleteDate=now WHERE l.lsId=:id")
    void deleteLastSchool(long id);
}
