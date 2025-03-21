package com.main.stpaul.dto.response;

import java.time.LocalDateTime;

import com.main.stpaul.constants.PaymetMode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptResponse {
    private String receiptId;
    private String receiptNo;
    private double amountPaid;
    private PaymetMode paymentMode;
    private LocalDateTime paymentDate;
    private String transactionId;
}
