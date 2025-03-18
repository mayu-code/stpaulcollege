package com.main.stpaul.services.impl;

import java.util.UUID;

import org.modelmapper.internal.bytebuddy.asm.Advice.Unused;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public LastSchool getLastSchoolById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLastSchoolById'");
    }

    @Override
    public LastSchool getLastSchoolByStudent(String studentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLastSchoolByStudent'");
    }
    
}
