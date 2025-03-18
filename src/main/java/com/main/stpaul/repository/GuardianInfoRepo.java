package com.main.stpaul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.main.stpaul.entities.GuardianInfo;

public interface GuardianInfoRepo extends JpaRepository<GuardianInfo,String>{
    
    @Query("SELECT g FROM GuardianInfo g WHERE g.student.id=:id AND g.isDelete=false")
    Optional<GuardianInfo> findByStudent(long id);

    @Modifying
    @Query("UPDATE GuardianInfo g SET g.isDelete=true AND g.deleteDate=now WHERE g.giId=:id")
    void deleteGuardianInfo(long id);
}
