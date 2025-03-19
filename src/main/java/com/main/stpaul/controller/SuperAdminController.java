package com.main.stpaul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.dto.ResponseDTO.SuccessResponse;
import com.main.stpaul.dto.request.CollegeFeesRequest;
import com.main.stpaul.entities.CollegeFees;
import com.main.stpaul.mapper.CollegeFeesMappler;
import com.main.stpaul.repository.AddressRepo;
import com.main.stpaul.services.impl.CollegeFeesServiceImpl;
import com.main.stpaul.services.impl.CollegeFessServiceImpl;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/superadmin")
public class SuperAdminController {

    
    @Autowired
    private CollegeFeesServiceImpl collegeFeesServiceImpl;

    @Autowired
    private CollegeFeesMappler collegeFeesMappler;

    
    @PostMapping("/college/fees")    
    public ResponseEntity<?> addCollegeFees(@RequestBody CollegeFeesRequest request)throws Exception{
        try {
            this.collegeFeesServiceImpl.addCollegeFees(this.collegeFeesMappler.toCollegeFees(request));
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Fees Added Successfully !");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
