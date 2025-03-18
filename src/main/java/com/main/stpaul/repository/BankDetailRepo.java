package com.main.stpaul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.main.stpaul.entities.BankDetail;

public interface BankDetailRepo extends JpaRepository<BankDetail,String>{
    

    @Query("SELECT b FROM BankDetail b WHERE b.student.id=:id AND b.isDelete=false")
    Optional<BankDetail> findByStudent(long id);


    @Modifying
    @Query("UPDATE BankDetail b SET b.isDelete=true AND b.deleteDate=now WHERE b.bankDetailId=:id")
    void delteBankDetail(long id);
}
