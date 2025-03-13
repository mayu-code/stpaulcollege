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
import com.main.stpaul.dto.ResponseDTO.SuccessResponse;
import com.main.stpaul.entities.User;
import com.main.stpaul.mapper.UserMapper;
import com.main.stpaul.services.impl.UserServiceImpl;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"",""})
public class UserController {
    
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserMapper userMapper;


    @GetMapping("/getProfile")
    @Operation(summary = "Get user by JwtToken", description = "Fetches user details by JwtToken")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization")String jwt){
        try {
            User user = this.userServiceImpl.getUserByJwt(jwt);
            DataResponse response = new DataResponse();
            response.setStatus(HttpStatus.OK);
            response.setMessage("Profile get successfully !");
            response.setStatusCode(200);
            response.setData(userMapper.toUserResponse(user));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage(e.getMessage());
            response.setStatusCode(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
