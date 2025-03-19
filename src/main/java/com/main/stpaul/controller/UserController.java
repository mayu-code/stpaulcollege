package com.main.stpaul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.dto.ResponseDTO.DataResponse;
import com.main.stpaul.entities.User;
import com.main.stpaul.mapper.UserMapper;
import com.main.stpaul.services.impl.CollegeFeesServiceImpl;
import com.main.stpaul.services.impl.UserServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = { "http://localhost:5173/", "http://localhost:5174/" })
public class UserController {
    
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private CollegeFeesServiceImpl collegeFeesServiceImpl;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getProfile")
    @Operation(summary = "Get user by JwtToken", description = "Fetches user details by JwtToken")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization")String jwt)throws Exception{
        log.info("Fetching profile Using JWT");
        try {
            User user = this.userServiceImpl.getUserByJwt(jwt);
            DataResponse response = DataResponse.builder()
                                                .data(userMapper.toUserResponse(user))
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("get Profile successfully !")
                                                .build();
            log.info("Profile fetched successfully for user: {}", user.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.info("Error to fetch profile : {}",e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/college/fees")
    public ResponseEntity<?> getAllCollegeFees()throws Exception{
        try {
            DataResponse response = DataResponse.builder()
                                                .data(this.collegeFeesServiceImpl.getAllCollegeFees())
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
