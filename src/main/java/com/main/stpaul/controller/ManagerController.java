package com.main.stpaul.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/manager")
@RestController
public class ManagerController {
    
    @GetMapping("/students")
    public ResponseEntity<?> allStudents(){
        return null;
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<?> studentById(){
        return null;
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<?> updateStudent(){
        return null;
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id")String id){
        return null;
    }
}
