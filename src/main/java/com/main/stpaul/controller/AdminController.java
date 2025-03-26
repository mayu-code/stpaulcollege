package com.main.stpaul.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.constants.Result;
import com.main.stpaul.constants.Status;
import com.main.stpaul.dto.response.StudentAcademicsResponse;
import com.main.stpaul.dto.response.StudentDetailResponse;
import com.main.stpaul.entities.Student;
import com.main.stpaul.entities.StudentAcademics;
import com.main.stpaul.helper.StudentHelper;
import com.main.stpaul.mapper.StudentAcademicsMapper;
import com.main.stpaul.mapper.StudentMapper;
import com.main.stpaul.services.impl.StudentAcademicsServiceImpl;
import com.main.stpaul.services.impl.StudentServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @Autowired
    private StudentAcademicsServiceImpl studentAcademicsServiceImpl;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentAcademicsMapper studentAcademicsMapper;

    
    @PostMapping("/student/promote")
    public ResponseEntity<?> promoteStudent(@RequestBody List<String> studentIds)throws Exception{
        try {
            for(String id:studentIds){
                StudentDetailResponse student = this.studentServiceImpl.getStudentById(id);
                if(student==null){
                    throw new EntityNotFoundException("Student not found !");
                }
                Student student1 = this.studentMapper.toStudent(student);
                student1.setStatus(Status.Ongoing);
                student1.setStdClass(String.valueOf(Integer.parseInt(student1.getStdClass())+1));
                student1.setSession(StudentHelper.sessionIncrementer(student1.getSession()));
                this.studentServiceImpl.promoteStudent(student1);

                StudentAcademicsResponse studentAcademics = this.studentAcademicsServiceImpl.getOngoingAcademicsByStudent(id);
                StudentAcademics academics = this.studentAcademicsMapper.toStudentAcademics(studentAcademics);
                academics.setStatus(Status.Completed);
                academics.setUpdatedDate(LocalDateTime.now());
                academics.setResult(Result.PASS);
                academics.setPromotionDate(LocalDate.now());
                this.studentAcademicsServiceImpl.updateStudentAcademics(academics);

                StudentAcademics newAcademics = new StudentAcademics();
                newAcademics.setStdClass(student1.getStdClass());
                newAcademics.setSession(student1.getSession());
                newAcademics.setStudent(student1);
                this.studentAcademicsServiceImpl.addStudentAcademics(newAcademics);
            }
            return ResponseEntity.status(200).body("Student promoted successfully !");
        } catch (Exception e) {
           throw new Exception(e.getMessage());
        }
    }
}
