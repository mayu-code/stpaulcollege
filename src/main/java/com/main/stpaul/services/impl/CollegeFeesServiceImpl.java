package com.main.stpaul.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.dto.response.CollegeFeesResponse;
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
    public List<CollegeFeesResponse> getAllCollegeFees() {
        return this.collegeFeesRepo.getAllCollegeFees();
    }

    @Override
    public CollegeFeesResponse getCollegeFees(long id) {
        return this.collegeFeesRepo.findCollegeFeesById(id).orElse(null);
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
