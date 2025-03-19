package com.main.stpaul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.main.stpaul.dto.response.GuardianInfoResponse;
import com.main.stpaul.entities.GuardianInfo;

public interface GuardianInfoRepo extends JpaRepository<GuardianInfo,String>{
    
    @Query("SELECT new com.main.stpaul.dto.response.GuardianInfoResponse(g.giId, g.name, g.phone, g.occupation, g.relation, g.income) " +
           "FROM GuardianInfo g WHERE g.student.id = :studentId AND g.isDelete = false")
    Optional<GuardianInfoResponse> findByStudentId(String studentId);

    @Modifying
    @Query("UPDATE GuardianInfo g SET g.isDelete=true AND g.deleteDate=now WHERE g.giId=:id")
    void deleteGuardianInfo(String id);
}
