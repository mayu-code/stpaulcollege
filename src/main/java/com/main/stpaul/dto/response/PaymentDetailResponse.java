package com.main.stpaul.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetailResponse {
    private String paymentDetailId;
    
    private double totalFees;
    private double balanceAmount;
    private double paidAmount;
    private String paymentType;
    private double installmentAmount;
    private int installmentGap;
    private int installments;
    private LocalDate dueDate;
    private List<ReceiptResponse> receipt;
}
