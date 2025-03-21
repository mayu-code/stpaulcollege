package com.main.stpaul.services.serviceInterface;


import java.util.List;

import com.main.stpaul.dto.response.ReceiptResponse;
import com.main.stpaul.entities.Receipt;

public interface ReceiptService {
    Receipt addReceipt(Receipt receipt);
    ReceiptResponse getReceiptById(String id);
    List<ReceiptResponse> getReceiptByPaymentDetail(String id);
}
