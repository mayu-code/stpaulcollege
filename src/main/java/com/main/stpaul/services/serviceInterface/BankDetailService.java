package com.main.stpaul.services.serviceInterface;

import com.main.stpaul.entities.BankDetail;

public interface BankDetailService {

    BankDetail addBankDetail(BankDetail bankDetail);
    BankDetail getBankDetailById(String bdId);
    BankDetail getBankDetailByStudent(String studentId);
}
