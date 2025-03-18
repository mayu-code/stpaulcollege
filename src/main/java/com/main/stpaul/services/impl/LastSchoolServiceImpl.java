package com.main.stpaul.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.dto.response.LastSchoolResponse;
import com.main.stpaul.entities.LastSchool;
import com.main.stpaul.repository.LastSchoolRepo;
import com.main.stpaul.services.serviceInterface.LastSchoolService;


@Service
public class LastSchoolServiceImpl implements LastSchoolService{

    @Autowired
    private LastSchoolRepo lastSchoolRepo;

    @Override
    public LastSchool addLastSchool(LastSchool lastSchool) {
        String id = UUID.randomUUID().toString();
        lastSchool.setLsId(id);
        return this.lastSchoolRepo.save(lastSchool);
    }

    @Override
    public LastSchoolResponse getLastSchoolById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLastSchoolById'");
    }

    @Override
    public LastSchoolResponse getLastSchoolByStudent(String studentId) {
        return this.lastSchoolRepo.findByStudent(studentId).orElse(null);
    }
    
}
