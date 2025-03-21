package com.main.stpaul.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.dto.ResponseDTO.DataResponse;
import com.main.stpaul.helper.PdfGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/accountant")
public class AccountantController {
    
    @GetMapping("/student/payment/receipt/{receiptId}")
    public ResponseEntity<?> downloadPdf(@PathVariable("receiptId")String receiptId)throws Exception{
        try {
            DataResponse response = DataResponse.builder()
                                                .data(PdfGenerator.generateReceiptPdf())
                                                .message("payment Receipt get Successfully !")
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}