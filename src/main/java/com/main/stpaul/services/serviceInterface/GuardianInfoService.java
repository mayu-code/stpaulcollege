package com.main.stpaul.services.serviceInterface;

import com.main.stpaul.dto.response.GuardianInfoResponse;
import com.main.stpaul.entities.GuardianInfo;

public interface GuardianInfoService {
    
    GuardianInfo addGuardianInfo(GuardianInfo guardianInfo);
    GuardianInfoResponse getGuardianInfoById(String id);
    GuardianInfoResponse getGuardianInfoByStudent(String studentId);
}
