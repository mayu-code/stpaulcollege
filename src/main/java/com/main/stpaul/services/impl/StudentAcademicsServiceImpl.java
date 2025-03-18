package com.main.stpaul.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.dto.response.StudentAcademicsResponse;
import com.main.stpaul.entities.StudentAcademics;
import com.main.stpaul.repository.StudentAcademicsRepo;
import com.main.stpaul.services.serviceInterface.StudentAcademicsService;

@Service
public class StudentAcademicsServiceImpl implements StudentAcademicsService{

    @Autowired
    private StudentAcademicsRepo studentAcademicsRepo;

    @Override
    public StudentAcademics addStudentAcademics(StudentAcademics studentAcademics) {
        String id = UUID.randomUUID().toString();
        studentAcademics.setStudentAcademicsId(id);
        return this.studentAcademicsRepo.save(studentAcademics);
    }

    @Override
    public List<StudentAcademicsResponse> getAcademicsByStudent(String studentId) {
        return this.studentAcademicsRepo.findByStudentId(studentId);
    }

    @Override
    public StudentAcademics getAcademicsById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAcademicsById'");
    }
    
}
