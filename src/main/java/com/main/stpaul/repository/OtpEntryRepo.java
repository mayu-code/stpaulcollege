package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.OtpEntry;


public interface OtpEntryRepo extends JpaRepository<OtpEntry,Long>{
    void deleteByEmail(String email);
    OtpEntry findByEmail(String email);
}
