package com.main.stpaul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.dto.ResponseDTO.DataResponse;
import com.main.stpaul.dto.response.ReceiptResponse;
import com.main.stpaul.dto.response.StudentDetailResponse;
import com.main.stpaul.entities.Receipt;
import com.main.stpaul.helper.PdfGenerator;
import com.main.stpaul.mapper.ReceiptMapper;
import com.main.stpaul.services.impl.PaymentDetailServiceImpl;
import com.main.stpaul.services.impl.ReceiptServiceImpl;
import com.main.stpaul.services.impl.StudentServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/accountant")
public class AccountantController {

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @Autowired
    private ReceiptServiceImpl receiptServiceImpl;

    @Autowired
    private PaymentDetailServiceImpl paymentDetailServiceImpl;

    @Autowired
    private ReceiptMapper receiptMapper ;
    
    @GetMapping("/student/{studentId}/payment/receipt/{receiptId}")
    public ResponseEntity<?> downloadPdf(@PathVariable("studentId")String studentId,@PathVariable("receiptId")String receiptId)throws Exception{
        try {
            StudentDetailResponse student = this.studentServiceImpl.getStudentById(studentId);
            if(student ==null){
                throw new EntityNotFoundException("Student not found !");
            }
            Receipt receipt = this.receiptServiceImpl.findByid(receiptId);
            String id = receipt.getPaymentDetail().getPaymentDetailId();

            DataResponse response = DataResponse.builder()
                                                .data(PdfGenerator.generateReceiptPdf(student,this.receiptMapper.toReceiptResponse(receipt),this.paymentDetailServiceImpl.getPaymentDetailByStudent(id)))
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