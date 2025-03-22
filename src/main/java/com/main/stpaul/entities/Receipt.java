package com.main.stpaul.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.stpaul.constants.PaymetMode;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Receipt {

    @Id
    private String receiptId;
    private String receiptNo;
    private double amountPaid;

    @Enumerated(EnumType.STRING)
    private PaymetMode paymentMode;
    private LocalDateTime paymentDate;
    private String transactionId;

    private boolean isDelete = false;
    private boolean isActive = true;

    private LocalDateTime addDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
    private LocalDateTime deleteDate;

    @ManyToOne
    @JoinColumn(name = "payment_detail_id")
    private PaymentDetail paymentDetail;
}
