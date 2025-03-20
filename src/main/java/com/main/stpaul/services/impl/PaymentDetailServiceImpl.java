package com.main.stpaul.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.entities.PaymentDetail;
import com.main.stpaul.repository.PaymentDetailRepo;
import com.main.stpaul.services.serviceInterface.PaymentDetailService;

@Service
public class PaymentDetailServiceImpl implements PaymentDetailService{

    @Autowired
    private PaymentDetailRepo paymentDetailRepo;

    @Override
    public PaymentDetail addPaymentDetail(PaymentDetail paymentDetail) {
        String id = UUID.randomUUID().toString();
        paymentDetail.setPaymentDetailId(id);
        return this.paymentDetailRepo.save(paymentDetail);
    }

    @Override
    public PaymentDetail getPaymentDetailByStudent(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPaymentDetailByStudent'");
    }

    @Override
    public PaymentDetail getPaymentById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPaymentById'");
    }

    @Override
    public boolean updatePaymentDetail(PaymentDetail paymentDetail, String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePaymentDetail'");
    }

    @Override
    public boolean deletePaymentDetail(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePaymentDetail'");
    }
    
}
