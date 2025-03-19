package com.main.stpaul.services.serviceInterface;

import java.util.List;

import com.main.stpaul.dto.response.CollegeFeesResponse;
import com.main.stpaul.entities.CollegeFees;

public interface CollegeFeesService {
    CollegeFees addCollegeFees(CollegeFees collegeFees);
    List<CollegeFeesResponse> getAllCollegeFees();
    CollegeFeesResponse getCollegeFees(long id);
    void updateCollegeFees(CollegeFees collegeFees);
    void deleteCollegeFees(long id);
}
