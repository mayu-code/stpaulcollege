package com.main.stpaul.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.dto.request.CollegeFeesRequest;
import com.main.stpaul.entities.CollegeFees;
import com.main.stpaul.repository.CollegeFeesRepo;
import com.main.stpaul.services.serviceInterface.CollegeFeesService;

@Service
public class CollegeFeesServiceImpl implements CollegeFeesService{

    @Autowired
    private CollegeFeesRepo collegeFeesRepo;

    @Override
    public CollegeFees addCollegeFees(CollegeFees collegeFees) {
        return this.collegeFeesRepo.save(collegeFees);
    }

    
    @Override
    public List<CollegeFees> getAllCollegeFees() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllCollegeFees'");
    }

    @Override
    public CollegeFees getCollegeFees(long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCollegeFees'");
    }

    @Override
    public void updateCollegeFees(CollegeFees collegeFees) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCollegeFees'");
    }

    @Override
    public void deleteCollegeFees(long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCollegeFees'");
    }
    
}
