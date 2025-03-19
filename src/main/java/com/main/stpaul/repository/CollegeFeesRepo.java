package com.main.stpaul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.stpaul.dto.response.CollegeFeesResponse;
import com.main.stpaul.entities.CollegeFees;

public interface CollegeFeesRepo extends JpaRepository<CollegeFees,Long>{
    
    @Query("""
        SELECT new com.main.stpaul.dto.response.CollegeFeesResponse(c.id,c.stdClass,c.totalFees,c.installmentGap,c.installmentsAmount) 
        FROM CollegeFees c
        WHERE c.isDelete=false
        """)
    List<CollegeFeesResponse> getAllCollegeFees();

    @Query("""
        SELECT new com.main.stpaul.dto.response.CollegeFeesResponse(
        c.id,c.stdClass,c.totalFees,c.installmentGap,c.installmentsAmount) 
        FROM CollegeFees c
        WHERE c.isDelete=false AND c.id=:id
        """)
    Optional<CollegeFeesResponse> findCollegeFeesById(Long id);

    @Query("""
        UPDATE CollegeFees c Set c.stdClass=:stdClass,c.totalFees=:totalFees,c.installmentGap=:installmentGap,
        c.installmentsAmount=:installmentsAmount
        WHERE c.id=:id            
            """)
    void updateCollegeFees(@Param("stdClass")String stdClass,
                            @Param("tatalFees")double totalFees,
                            @Param("installmentGap")int installmentGap,
                            @Param("installmentsAmount")double installmentsAmount,
                            @Param("id")long id
                            );

}
