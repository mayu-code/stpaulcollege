package com.main.stpaul.services.serviceInterface;

import com.main.stpaul.entities.PaymentDetail;

public interface PaymentDetailService {
    PaymentDetail addPaymentDetail(PaymentDetail paymentDetail);
    PaymentDetail getPaymentDetailByStudent(String id);
    PaymentDetail getPaymentById(String id);
    boolean updatePaymentDetail(PaymentDetail paymentDetail,String id);
    boolean deletePaymentDetail(String id);
}
