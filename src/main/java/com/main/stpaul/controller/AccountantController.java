package com.main.stpaul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.dto.ResponseDTO.DataResponse;
import com.main.stpaul.entities.Receipt;
import com.main.stpaul.entities.Student;
import com.main.stpaul.helper.PdfGenerator;
import com.main.stpaul.mapper.ReceiptMapper;
import com.main.stpaul.mapper.StudentMapper;
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
    private ReceiptMapper receiptMapper;

    @Autowired
    private StudentMapper studentMapper;
    
    @GetMapping("/student/{studentId}/payment/receipt/{receiptId}")
    public ResponseEntity<?> downloadPdf(@PathVariable("studentId")String studentId,@PathVariable("receiptId")String receiptId)throws Exception{
        log.info("Starting downloadPdf method with studentId: {} and receiptId: {}", studentId, receiptId);
        try {
            Student student = this.studentServiceImpl.getStudentById(studentId);
            if(student ==null){
                log.error("Student not found with ID: {}", studentId);
                throw new EntityNotFoundException("Student not found !");
            }
            Receipt receipt = this.receiptServiceImpl.findByid(receiptId);
            String id = receipt.getPaymentDetail().getPaymentDetailId();

            byte[] pdf = PdfGenerator.generateReceiptPdf(student,this.receiptMapper.toReceiptResponse(receipt),this.paymentDetailServiceImpl.getPaymentById(id));

            DataResponse response = DataResponse.builder()
                                                .data(pdf)
                                                .message("payment Receipt get Successfully !")
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .build();
            log.info("Successfully generated PDF for studentId: {} and receiptId: {}", studentId, receiptId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error generating PDF for studentId: {} and receiptId: {}: {}", studentId, receiptId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/student/pending")
    public ResponseEntity<?> getPendingStudents()throws Exception{
        log.info("Starting getPendingStudents method");
        try {
            DataResponse response = DataResponse.builder()
                                                .data(this.studentServiceImpl.getPendingStudents())
                                                .message("get All Pending Students successfully !")
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .build();
            log.info("Successfully retrieved pending students");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error retrieving pending students: {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

}