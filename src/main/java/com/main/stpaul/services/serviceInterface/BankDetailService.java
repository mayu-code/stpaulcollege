package com.main.stpaul.services.serviceInterface;

import com.main.stpaul.dto.request.BankDetailRequest;
import com.main.stpaul.dto.response.BankDetailResponse;
import com.main.stpaul.entities.BankDetail;

public interface BankDetailService {

    BankDetail addBankDetail(BankDetail bankDetail);
    BankDetailResponse getBankDetailById(String bdId);
    BankDetailResponse getBankDetailByStudent(String studentId);

    void updateBankDetail(BankDetailRequest bankDetail,String id);
    void deleteBankDetail(String id);
}
