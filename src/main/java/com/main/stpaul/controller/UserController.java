package com.main.stpaul.controller;

import java.awt.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.dto.ResponseDTO.DataResponse;
import com.main.stpaul.dto.response.StudentAcademicsResponse;
import com.main.stpaul.dto.response.StudentDetailResponse;
import com.main.stpaul.entities.User;
import com.main.stpaul.helper.PdfGenerator;
import com.main.stpaul.mapper.UserMapper;
import com.main.stpaul.services.impl.CollegeFeesServiceImpl;
import com.main.stpaul.services.impl.PaymentDetailServiceImpl;
import com.main.stpaul.services.impl.ReceiptServiceImpl;
import com.main.stpaul.services.impl.StudentAcademicsServiceImpl;
import com.main.stpaul.services.impl.StudentServiceImpl;
import com.main.stpaul.services.impl.UserServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = { "http://localhost:5173/", "http://localhost:5174/" })
public class UserController {
    
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private StudentAcademicsServiceImpl studentAcademicsServiceImpl;

    @Autowired
    private CollegeFeesServiceImpl collegeFeesServiceImpl;

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @Autowired
    private PaymentDetailServiceImpl paymentDetailServiceImpl;

    @Autowired
    private ReceiptServiceImpl receiptServiceImpl;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getProfile")
    @Operation(summary = "Get user by JwtToken", description = "Fetches user details by JwtToken")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization")String jwt)throws Exception{
        log.info("Fetching profile Using JWT");
        try {
            User user = this.userServiceImpl.getUserByJwt(jwt);
            DataResponse response = DataResponse.builder()
                                                .data(userMapper.toUserResponse(user))
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("get Profile successfully !")
                                                .build();
            log.info("Profile fetched successfully for user: {}", user.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error to fetch profile : {}",e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/college/fees")
    public ResponseEntity<?> getAllCollegeFees()throws Exception{
        log.info("Fetching College fees");
        try {
            DataResponse response = DataResponse.builder()
                                                .data(this.collegeFeesServiceImpl.getAllCollegeFees())
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("get All Fees successfully !")
                                                .build();
            log.info("Fetch College fees Successfully ");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error to fetch College fees", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/college/fees/{stdClass}")
    public ResponseEntity<?> getAllCollegeFeesByClass(@PathVariable("stdClass")String stdClass)throws Exception{
        log.info("Fetching College fees By Class : {}",stdClass);
        try {
            DataResponse response = DataResponse.builder()
                                                .data(this.collegeFeesServiceImpl.getCollegeFeesByClass(stdClass))
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("get All Fees successfully !")
                                                .build();
            log.info("Fetched College fees Successfully by Class : {}",stdClass);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error to fetch College fees", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/college/classes")
    public ResponseEntity<?> getDistinctClasses()throws Exception{
        try {
            DataResponse response = DataResponse.builder()
                                                .data(this.collegeFeesServiceImpl.distinctClasses())
                                                .message("Get All Distinct Classes")
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<?> studentById(@PathVariable("id")String id)throws Exception{
        log.info("Student Detail Fetching for Id : {}",id);
        try {
                StudentDetailResponse student = this.studentServiceImpl.getData(id);

            DataResponse response = DataResponse.builder()
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("Get All Users Successfully !")
                                                .data(student)
                                                .build();
            log.info("Student Detail Fetched for Id : {}",id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}",e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateReceipt() {
        byte[] pdfBytes = PdfGenerator.generateReceiptPdf();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=receipt.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/students")
    public ResponseEntity<?> allStudents() throws Exception{
        log.info("All Fetching Students .....");
        try {
            DataResponse response = DataResponse.builder()
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("Get All Users Successfully !")
                                                .data(this.studentServiceImpl.getAllStudents())
                                                .build();
            log.info("All Students Fetched Successfully ");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}",e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/students/{studentId}/academics")
    public ResponseEntity<?> getAcademicsDetail(@PathVariable("studentId")String studentId)throws Exception{
        try {
            java.util.List<StudentAcademicsResponse> students =this.studentAcademicsServiceImpl.getAcademicsByStudent(studentId);
            for(StudentAcademicsResponse st:students){
                st.setPaymentDetail(this.paymentDetailServiceImpl.getPaymentDetailByStudent(st.getStudentAcademicsId()));
                st.getPaymentDetail().setReceipt(this.receiptServiceImpl.getReceiptByPaymentDetail(st.getPaymentDetail().getPaymentDetailId()));
            }
            DataResponse response = DataResponse.builder()
                                                .data(students)
                                                .message("Get All Academics Successfully !")
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
