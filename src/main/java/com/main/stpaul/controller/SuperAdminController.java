package com.main.stpaul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.dto.ResponseDTO.DataResponse;
import com.main.stpaul.dto.ResponseDTO.SuccessResponse;
import com.main.stpaul.dto.request.CollegeFeesRequest;
import com.main.stpaul.mapper.CollegeFeesMappler;
import com.main.stpaul.services.impl.CollegeFeesServiceImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

    @GetMapping("/college/fees/{id}")    
    public ResponseEntity<?> addCollegeFeesById(@PathVariable("id")long id)throws Exception{
        try {
            DataResponse response = DataResponse.builder()
                                                .data(this.collegeFeesServiceImpl.getCollegeFees(id))
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("get All Fees successfully !")
                                                .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
