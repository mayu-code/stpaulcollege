package com.main.stpaul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.main.stpaul.entities.PaymentDetail;

public interface PaymentDetailRepo extends JpaRepository<PaymentDetail,String>{

    @Query("""
        SELECT p
        From PaymentDetail p
        WHERE p.studentAcademics.studentAcademicsId=:academicsId AND p.isDelete=false
        ORDER BY p.addDate
        """)
    Optional<PaymentDetail> findByStudentAcademics(String academicsId);

    @Query("""
        SELECT p
        From PaymentDetail p
        WHERE p.paymentDetailId=:id AND p.isDelete=false
        ORDER BY p.addDate
        """)
    Optional<PaymentDetail> findByPaymentId(String id);
}
