package com.main.stpaul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.main.stpaul.repository.AddressRepo;
import com.main.stpaul.services.impl.CollegeFeesServiceImpl;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/superadmin")
public class SuperAdminController {

    
    @Autowired
    private CollegeFeesServiceImpl collegeFeesServiceImpl;

    
    // public ResponseEntity<?> addCollegeFees(@RequestBody null){
    //     try {
    //         return null;
    //     } catch (Exception e) {
    //         throw new Exception(e.getMessage());
    //     }
    // }

}
