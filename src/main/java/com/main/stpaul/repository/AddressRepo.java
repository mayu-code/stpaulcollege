package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.stpaul.entities.Address;

public interface AddressRepo extends JpaRepository<Address,String>{
    
}
