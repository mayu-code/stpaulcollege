package com.main.stpaul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.main.stpaul.dto.response.PaymentDetailResponse;
import com.main.stpaul.entities.PaymentDetail;

public interface PaymentDetailRepo extends JpaRepository<PaymentDetail,String>{

    @Query("""
        SELECT new com.main.stpaul.dto.response.PaymentDetailResponse(p.paymentDetailId,p.totalFees,p.balanceAmount,
        p.paidAmount,p.paymentType,p.installmentAmount,p.installmentGap,p.installments,p.dueDate,null)
        From PaymentDetail p
        WHERE p.studentAcademics.studentAcademicsId=:academicsId AND p.isDelete=false
        ORDER BY p.addDate
        """)
    Optional<PaymentDetailResponse> findByStudentAcademics(String academicsId);

    @Query("""
        SELECT new com.main.stpaul.dto.response.PaymentDetailResponse(p.paymentDetailId,p.totalFees,p.balanceAmount,
        p.paidAmount,p.paymentType,p.installmentAmount,p.installmentGap,p.installments,p.dueDate,null)
        From PaymentDetail p
        WHERE p.paymentDetailId=:id AND p.isDelete=false
        ORDER BY p.addDate
        """)
    Optional<PaymentDetailResponse> findByPaymentId(String id);
}
