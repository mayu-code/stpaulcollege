package com.main.stpaul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.main.stpaul.entities.Address;

import jakarta.transaction.Transactional;

public interface AddressRepo extends JpaRepository<Address,String>{
    
    @Query("SELECT a FROM Address a Where a.student.id=:id AND a.isDelete=false")
    List<Address> findAddressByStudent(long id);

    @Query("SELECT a FROM Address a Where a.addressId=:id AND a.isDelete=false ")
    Optional<Address> findById(long id);
    
    @Transactional
    @Modifying
    @Query("UPDATE Address a SET a.isDelete=true AND a.deleteDate=now WHERE a.addressId=:id")
    void deleteAddress(long id);
}
