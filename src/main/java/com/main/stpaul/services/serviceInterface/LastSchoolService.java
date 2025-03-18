package com.main.stpaul.services.serviceInterface;

import com.main.stpaul.dto.response.LastSchoolResponse;
import com.main.stpaul.entities.LastSchool;

public interface LastSchoolService {

    LastSchool addLastSchool(LastSchool lastSchool);
    LastSchoolResponse getLastSchoolById(String id);
    LastSchoolResponse getLastSchoolByStudent(String studentId);
    
}
