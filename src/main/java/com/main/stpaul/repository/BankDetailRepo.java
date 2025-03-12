package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.BankDetail;

public interface BankDetailRepo extends JpaRepository<BankDetail,String>{
    
}
