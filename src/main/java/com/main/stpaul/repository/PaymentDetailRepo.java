package com.main.stpaul.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.main.stpaul.dto.response.PaymentDetailResponse;
import com.main.stpaul.entities.PaymentDetail;

public interface PaymentDetailRepo extends JpaRepository<PaymentDetail,String>{
    

    @Query("""
        SELECT new com.main.stpaul.dto.response.PaymentDetailResponse(p.paymentDetailId,p.totalFees,p.balanceAmount,
        p.paidAmount,p.paymentType,p.installmentAmount,p.installmentGap,p.installments,p.dueDate)
        From PaymentDetail p
        WHERE p.studentAcademics.studentAcademicsId=:academicsId AND p.isDelete=false
        ORDER BY p.addDate
        """)
    Optional<PaymentDetailResponse> findByStudentAcademics(String academicsId);
    // private String paymentDetailId;
    
    // private double totalFees;
    // private double balanceAmount;
    // private double paidAmount;
    // private String paymentType;
    // private double installmentAmount;
    // private int installmentGap;
    // private int installments;
    // private LocalDate dueDate;
    // private String stdClass;
}
