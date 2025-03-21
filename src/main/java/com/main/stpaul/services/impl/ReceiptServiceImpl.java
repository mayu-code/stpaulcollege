package com.main.stpaul.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.dto.response.ReceiptResponse;
import com.main.stpaul.entities.Receipt;
import com.main.stpaul.repository.ReceiptRepo;
import com.main.stpaul.services.serviceInterface.ReceiptService;

@Service
public class ReceiptServiceImpl implements ReceiptService{

    @Autowired
    private ReceiptRepo receiptRepo;

    @Override
    public Receipt addReceipt(Receipt receipt) {
        String id = UUID.randomUUID().toString();
        receipt.setReceiptId(id);
        return this.receiptRepo.save(receipt);
    }

    @Override
    public ReceiptResponse getReceiptById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReceiptById'");
    }

    @Override
    public List<ReceiptResponse> getReceiptByPaymentDetail(String id) {
        return this.receiptRepo.findByPaymentDetail(id);
    }
    
}
