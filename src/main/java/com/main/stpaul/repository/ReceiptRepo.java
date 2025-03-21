package com.main.stpaul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.main.stpaul.dto.response.ReceiptResponse;
import com.main.stpaul.entities.Receipt;

public interface ReceiptRepo extends JpaRepository<Receipt,String>{
    

    @Query("""
            SELECT new com.main.stpaul.dto.response.ReceiptResponse(r.receiptId,r.receiptNo,r.amountPaid,r.paymentMode,
            r.paymentDate,r.transactionId)
            FROM Receipt r
            WHERE r.paymentDetail.paymentDetailId=:pdId AND r.isDelete=false
            """)
    List<ReceiptResponse> findByPaymentDetail(String pdId);

    @Query("""
        SELECT new com.main.stpaul.dto.response.ReceiptResponse(r.receiptId,r.receiptNo,r.amountPaid,r.paymentMode,
        r.paymentDate,r.transactionId)
        FROM Receipt r
        WHERE r.receiptId=:id AND r.isDelete=false
        """)
    Optional<ReceiptResponse> findByReceiptId(String id);

    // private String receiptId;
    // private String receiptNo;
    // private double amountPaid;
    // private String paymentMode;
    // private LocalDateTime paymentDate;
    // private String transactionId;
}
