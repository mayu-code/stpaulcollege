package com.main.stpaul.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.main.stpaul.constants.PaymetMode;
import com.main.stpaul.constants.Status;
import com.main.stpaul.dto.ResponseDTO.DataResponse;
import com.main.stpaul.dto.ResponseDTO.SuccessResponse;
import com.main.stpaul.dto.request.BankDetailRequest;
import com.main.stpaul.dto.request.GuardianInfoRequest;
import com.main.stpaul.dto.request.LastSchoolRequest;
import com.main.stpaul.dto.request.PaymentDetailRequest;
import com.main.stpaul.dto.request.StudentAddRequest;
import com.main.stpaul.dto.request.StudentRequest;
import com.main.stpaul.dto.response.StudentAcademicsResponse;
import com.main.stpaul.dto.response.StudentDetailResponse;
import com.main.stpaul.entities.AdmissionForm;
import com.main.stpaul.entities.BankDetail;
import com.main.stpaul.entities.BiofocalSubject;
import com.main.stpaul.entities.Documents;
import com.main.stpaul.entities.GuardianInfo;
import com.main.stpaul.entities.LastSchool;
import com.main.stpaul.entities.PaymentDetail;
import com.main.stpaul.entities.Receipt;
import com.main.stpaul.entities.Stream;
import com.main.stpaul.entities.Student;
import com.main.stpaul.entities.StudentAcademics;
import com.main.stpaul.entities.Subject;
import com.main.stpaul.helper.PdfGenerator;
import com.main.stpaul.mapper.AdmissionFromMapper;
import com.main.stpaul.mapper.BankDetailMapper;
import com.main.stpaul.mapper.GuardianInfoMapper;
import com.main.stpaul.mapper.LastSchoolMapper;
import com.main.stpaul.mapper.PaymentDetailMapper;
import com.main.stpaul.mapper.ReceiptMapper;
import com.main.stpaul.mapper.StudentAcademicsMapper;
import com.main.stpaul.mapper.StudentMapper;

import com.main.stpaul.services.impl.AdmissionFormServiceImpl;
import com.main.stpaul.services.impl.BankDetailServiceImpl;
import com.main.stpaul.services.impl.BioFocalSubjectServiceImpl;
import com.main.stpaul.services.impl.CollegeFeesServiceImpl;
import com.main.stpaul.services.impl.DocumentServiceImpl;
import com.main.stpaul.services.impl.GuardianInfoServiceImpl;
import com.main.stpaul.services.impl.LastSchoolServiceImpl;
import com.main.stpaul.services.impl.PaymentDetailServiceImpl;
import com.main.stpaul.services.impl.ReceiptServiceImpl;
import com.main.stpaul.services.impl.StreamServiceImpl;
import com.main.stpaul.services.impl.StudentAcademicsServiceImpl;
import com.main.stpaul.services.impl.StudentServiceImpl;
import com.main.stpaul.services.impl.SubjectServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;

@Slf4j
@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @Autowired
    private LastSchoolServiceImpl lastSchoolServiceImpl;

    @Autowired
    private BankDetailServiceImpl bankDetailServiceImpl;

    @Autowired
    private GuardianInfoServiceImpl guardianInfoServiceImpl;

    @Autowired
    private AdmissionFormServiceImpl admissionFormServiceImpl;

    @Autowired
    private StudentAcademicsServiceImpl studentAcademicsServiceImpl;

    @Autowired
    private SubjectServiceImpl subjectServiceImpl;

    @Autowired
    private ReceiptServiceImpl receiptServiceImpl;

    @Autowired
    private StreamServiceImpl streamServiceImpl;

    @Autowired
    private DocumentServiceImpl documentServiceImpl;

    @Autowired
    private PaymentDetailServiceImpl paymentDetailServiceImpl;

    @Autowired
    private CollegeFeesServiceImpl collegeFeesServiceImpl;

    @Autowired
    private BioFocalSubjectServiceImpl bioFocalSubjectServiceImpl;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private BankDetailMapper bankDetailMapper;

    @Autowired
    private LastSchoolMapper lastSchoolMapper;

    @Autowired
    private GuardianInfoMapper guardianInfoMapper;

    @Autowired
    private AdmissionFromMapper admissionFromMapper;

    @Autowired
    private PaymentDetailMapper paymentDetailMapper;

    @Autowired
    private StudentAcademicsMapper studentAcademicsMapper;

    @Autowired
    private ReceiptMapper receiptMapper;


    // Post Apis *********************

    @PostMapping("/student")
    @Operation(summary = "Register a new student", description = "Registers a new student with the provided details and optional image")
    public ResponseEntity<?> registerStudent(@RequestPart("studentAdd")StudentAddRequest request,@RequestPart(value = "image",required = false)MultipartFile image)throws Exception{
        log.info("Starting registerStudent method");
        try {
            Student student=this.studentMapper.toStudent(request.getStudent());
            student.setSession(request.getAdmissionForm().getSession());
            student.setAdmissionDate(request.getAdmissionForm().getAdmissionDate());
            student.setStdClass(request.getAdmissionForm().getStdClass());
            try {
                student.setImage(image.getBytes());
            } catch (Exception e) {
                student.setImage(null);
            }
            student=this.studentServiceImpl.addStudent(student);
            log.info("Student Added Successfully ");

            AdmissionForm admissionForm = this.admissionFromMapper.toAdmissionForm(request.getAdmissionForm());
            admissionForm.setStudent(student);
            this.admissionFormServiceImpl.addAdmissionForm(admissionForm);
            log.info("admission form Added Successfully ");

            BankDetail bankDetail = this.bankDetailMapper.toBankDetail(request.getBankDetail());
            bankDetail.setStudent(student);
            this.bankDetailServiceImpl.addBankDetail(bankDetail);
            log.info("Bank Detail Added Successfully ");

            LastSchool lastSchool = this.lastSchoolMapper.toLastSchool(request.getLastSchool());
            lastSchool.setStudent(student);
            this.lastSchoolServiceImpl.addLastSchool(lastSchool);
            log.info("Last School Detail Added Successfully ");

            GuardianInfo guardianInfo = this.guardianInfoMapper.toGuardianInfo(request.getGuardianInfo());
            guardianInfo.setStudent(student);
            this.guardianInfoServiceImpl.addGuardianInfo(guardianInfo);
            log.info("Guardian Into Added Successfully ");

            StudentAcademics studentAcademics = new StudentAcademics();
            studentAcademics.setStdClass(admissionForm.getStdClass());
            studentAcademics.setStudent(student);
            this.studentAcademicsServiceImpl.addStudentAcademics(studentAcademics);
            log.info("Academic Detail Added Successfully ");

            Stream stream = new Stream();
            stream.setAcademics(studentAcademics);
            stream.setMedium(request.getSubject().getMedium());
            stream.setStream(request.getSubject().getStream());
            stream.setSubStream(request.getSubject().getSubStream());
            stream = this.streamServiceImpl.addStream(stream);
            for(String subject:request.getSubject().getSubjects()){
                Subject sb = new Subject();
                sb.setName(subject);
                sb.setStream(stream);
                this.subjectServiceImpl.addSubject(sb);
            }
            log.info("Subjects Added Successfully ");

            BiofocalSubject biofocalSubject = new BiofocalSubject();
            biofocalSubject.setMedium(request.getBioFocalSubject().getMedium());
            biofocalSubject.setAcademics(studentAcademics);
            biofocalSubject.setSubStream(request.getBioFocalSubject().getSubStream());
            biofocalSubject.setSubject(request.getBioFocalSubject().getSubject());
            this.bioFocalSubjectServiceImpl.addBiofocalSubject(biofocalSubject);
            log.info("Bio Focal Subject Added Successfully ");

            DataResponse response = DataResponse.builder()
                                                .data(student.getStudentId())
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("Student Register Successfully !")
                                                .build();
            log.info("Student Admission Successfully ");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error Occured While addmision of Student");
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/students/{id}/documents")
    @Operation(summary = "Upload student documents", description = "Uploads documents for a specific student by their ID")
    public ResponseEntity<?> uploadDoucuments(@PathVariable("id")String id,
                                            @RequestParam Map<String, MultipartFile> files)throws Exception{
        log.info("Starting uploadDoucuments method with studentId: {}", id);
        log.info("Uploading Student Documents");
        StudentDetailResponse student = this.studentServiceImpl.getStudentById(id);
        if(student ==null){
            log.warn("student not found for id : {}",id);
            throw new EntityNotFoundException("Student not present !");
        }                                         

        files.forEach((docName,file)->{
            try{
                Documents document = new Documents();
                document.setDocumentType(docName);
                document.setDocument(file.getBytes());
                document.setStudent(this.studentMapper.toStudent(student));
                this.documentServiceImpl.addDocuments(document);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"document uploaded Successfully !");   
        log.info("document uploaded Successfully ");
        return ResponseEntity.status(HttpStatus.OK).body(response);  
    }
    
    @PostMapping("/students/{studentId}/academics/{academicId}/payment-detail")
    @Operation(summary = "Add payment detail", description = "Adds payment details for a specific student and academic record")
    public ResponseEntity<?> addPaymentDetail(@PathVariable("studentId")String studentId,@PathVariable("academicId")String academicId,@RequestBody PaymentDetailRequest paymentDetail)throws Exception{
        log.info("Starting addPaymentDetail method with studentId: {} and academicId: {}", studentId, academicId);
       try {
        StudentAcademics academics = this.studentAcademicsMapper.toStudentAcademics(this.studentAcademicsServiceImpl.getAcademicsById(academicId));
        PaymentDetail paymentDetail2 = this.paymentDetailMapper.toPaymentDetail(paymentDetail);
        paymentDetail2.setStudentAcademics(academics);
        Receipt receipt = new Receipt();

        receipt.setAmountPaid(paymentDetail2.getPaidAmount());
        receipt.setTransactionId(null);
        receipt.setPaymentMode(PaymetMode.valueOf(paymentDetail2.getPaymentType()));
        receipt.setPaymentDate(LocalDateTime.now());
        
        paymentDetail2= this.paymentDetailServiceImpl.addPaymentDetail(paymentDetail2);
        receipt.setPaymentDetail(paymentDetail2);
        receipt = this.receiptServiceImpl.addReceipt(receipt);
        StudentDetailResponse student =  this.studentServiceImpl.getStudentById(studentId);
        Student student2 = this.studentMapper.toStudent(student);
        student2.setStatus(Status.Ongoing);
        this.studentServiceImpl.updateStudent(student2);
        byte[] pdfBytes = PdfGenerator.generateReceiptPdf(this.studentServiceImpl.getStudentById(studentId),receiptMapper.toReceiptResponse(receipt),paymentDetailMapper.toPaymentDetailResponse(paymentDetail2));
        DataResponse response = DataResponse.builder()
                                            .data(pdfBytes)
                                            .message("payment detail added Successfully")
                                            .status(HttpStatus.OK)
                                            .statusCode(200)
                                            .build();
        log.info("Successfully added payment detail for studentId: {} and academicId: {}", studentId, academicId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
       } catch (Exception e) {
            log.error("Error adding payment detail for studentId: {} and academicId: {}: {}", studentId, academicId, e.getMessage());
            throw new Exception(e.getMessage());
       }
    }

    // Get API's ***************************

    @GetMapping("/students/{id}")
    @Operation(summary = "Get student by ID", description = "Fetches details of a specific student by their ID")
    public ResponseEntity<?> studentById(@PathVariable("id")String id)throws Exception{
        log.info("Starting studentById method with studentId: {}", id);
        log.info("Student Detail Fetching for Id : {}",id);
        try {
                StudentDetailResponse student = this.studentServiceImpl.getStudentById(id);
                student.setStudentAcademics(this.studentAcademicsServiceImpl.getAcademicsByStudent(id));
                student.setGuardianInfo(this.guardianInfoServiceImpl.getGuardianInfoByStudent(id));
                student.setBankDetail(this.bankDetailServiceImpl.getBankDetailByStudent(id));
                student.setLastSchool(this.lastSchoolServiceImpl.getLastSchoolByStudent(id));
                student.setDocuments(this.documentServiceImpl.getStudentDocuments(id));

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


    // Put API's *************************************

    @PutMapping("/students/{id}")
    @Operation(summary = "Update student details", description = "Updates the details of a specific student by their ID")
    public ResponseEntity<?> updateStudent(@PathVariable("id")String id,@RequestBody StudentRequest request)throws Exception{
        log.info("Starting updateStudent method with studentId: {}", id);
        log.info("Updating Student Detail for Student ID : {}",id);
        try {
            Student student = this.studentServiceImpl.findById(id);
            if(student ==null){
                log.warn("student not found with id : {}", id);
                throw new EntityNotFoundException("Student not present !");
            } 
            student.setFirstName(request.getFirstName());
            student.setFatherName(request.getFatherName());
            student.setMotherName(request.getMotherName());
            student.setSurname(request.getSurname());
            student.setEmail(request.getEmail());
            student.setPhoneNo(request.getPhoneNo());
            student.setDateOfBirth(request.getDateOfBirth());
            student.setGender(request.getGender());
            student.setAdharNo(request.getAdharNo());
            student.setBloodGroup(request.getBloodGroup());
            student.setCaste(request.getCaste());
            student.setCategory(request.getCategory());
            student.setScholarshipCategory(request.getScholarshipCategory());
            student.setUpdatedDate(LocalDateTime.now());
            this.studentServiceImpl.updateStudent(student);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Student detail updated Successfully !");
            log.info("Updated Student Detail for Student ID : {}",id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}",e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/{studentId}/bank-detail/{bkId}")
    @Operation(summary = "Update bank details", description = "Updates the bank details of a specific student by their ID and bank detail ID")
    public ResponseEntity<?> updateBankDetail(@PathVariable("studentId")String studentId,@PathVariable("bkId")String bkId,
                                                @RequestBody BankDetailRequest bankDetail)throws Exception{
        log.info("Starting updateBankDetail method with studentId: {} and bankDetailId: {}", studentId, bkId);
        log.info("Updating Student bank Detail for Student ID : {}",studentId);
        try {
            StudentDetailResponse student = this.studentServiceImpl.getStudentById(studentId);
            if(student ==null){
                log.warn("student not found with id : {}", studentId);
                throw new EntityNotFoundException("Student not present !");
            } 
            this.bankDetailServiceImpl.updateBankDetail(bankDetail, bkId);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"bank detail updated Successfully !");
            log.info("Updated Student bank Detail for Student ID : {}",studentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}",e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/{studentId}/last-school/{lsId}")
    @Operation(summary = "Update last school details", description = "Updates the last school details of a specific student by their ID and last school ID")
    public ResponseEntity<?> updateLastSchool(@PathVariable("studentId")String studentId,@PathVariable("lsId")String lsId,
                                                @RequestBody LastSchoolRequest lastSchool)throws Exception{
        log.info("Starting updateLastSchool method with studentId: {} and lastSchoolId: {}", studentId, lsId);
        log.info("Updating Student Last School Detail for Student Id :{}",studentId);
        try {
            StudentDetailResponse student = this.studentServiceImpl.getStudentById(studentId);
            if(student ==null){
                log.warn("student not found with id : {}", studentId);
                throw new EntityNotFoundException("Student not present !");
            }
            this.lastSchoolServiceImpl.updateLastSchool(lastSchool, lsId);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Last School Detail updated Successfully !");
            log.info("Updated Student Last School Detail for Student Id :{}",studentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}",e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/{studentId}/guardian-info/{giId}")
    @Operation(summary = "Update guardian info", description = "Updates the guardian information of a specific student by their ID and guardian info ID")
    public ResponseEntity<?> updateGuardianInfo(@PathVariable("studentId")String studentId,@PathVariable("giId")String giId,
                                                @RequestBody GuardianInfoRequest guardianInfo)throws Exception{
        log.info("Starting updateGuardianInfo method with studentId: {} and guardianInfoId: {}", studentId, giId);
        log.info("Updating Student Guardian Info for ID : {}",studentId);
        try {
            StudentDetailResponse student = this.studentServiceImpl.getStudentById(studentId);
            if(student ==null){
                log.warn("student not found with id : {}", studentId);
                throw new EntityNotFoundException("Student not present !");
            }
            this.guardianInfoServiceImpl.updateGuardianInfo(guardianInfo,giId);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Guardian information updated Successfully !");
            log.info("Updated Student Guardian Info for ID : {}",studentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}",e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/{studentId}/image")
    @Operation(summary = "Update student image", description = "Updates the image of a specific student by their ID")
    public ResponseEntity<?> updateImage(@PathVariable("studentId")String studentId,@RequestPart("image")MultipartFile image)throws Exception{
        log.info("Starting updateImage method with studentId: {}", studentId);
        log.info("Updating Student Image for ID : {}",studentId);
        try {
            Student student = this.studentServiceImpl.findById(studentId);
            if(student ==null){
                log.warn("student not found with id : {}", studentId);
                throw new EntityNotFoundException("Student not present !");
            }
            student.setImage(image.getBytes());
            this.studentServiceImpl.updateStudent(student);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Image updated Successfully !");
            log.info("Updated Student Image for ID : {}",studentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error While Fetching Students {}",e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
    // Delete API's ********************************

    @DeleteMapping("/students/{id}")
    @Operation(summary = "Delete student", description = "Deletes a specific student by their ID")
    public ResponseEntity<?> deleteStudent(@PathVariable("id")String id)throws Exception{
        log.info("Starting deleteStudent method with studentId: {}", id);
        try {
            this.studentServiceImpl.deleteStudent(id);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Student deleted Successfully !");
            log.info("Successfully deleted student with ID: {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error deleting student with ID: {}: {}", id, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/students/bank-detail/{bkId}")
    @Operation(summary = "Delete bank detail", description = "Deletes the bank detail of a specific student by the bank detail ID")
    public ResponseEntity<?> deleteBankDetail(@PathVariable("bkId")String bkId)throws Exception{
        log.info("Starting deleteBankDetail method with bankDetailId: {}", bkId);
        try {
            this.bankDetailServiceImpl.deleteBankDetail(bkId);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Student deleted Successfully !");
            log.info("Successfully deleted bank detail with ID: {}", bkId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error deleting bank detail with ID: {}: {}", bkId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/students/guardian-info/{giId}")
    @Operation(summary = "Delete guardian info", description = "Deletes the guardian information of a specific student by the guardian info ID")
    public ResponseEntity<?> deleteGuardianInfo(@PathVariable("giId")String giId)throws Exception{
        log.info("Starting deleteGuardianInfo method with guardianInfoId: {}", giId);
        try {
            this.guardianInfoServiceImpl.deleteGuardianInfo(giId);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Student deleted Successfully !");
            log.info("Successfully deleted guardian info with ID: {}", giId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error deleting guardian info with ID: {}: {}", giId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/students/last-school/{lsId}")
    @Operation(summary = "Delete last school detail", description = "Deletes the last school detail of a specific student by the last school ID")
    public ResponseEntity<?> deleteLastSchool(@PathVariable("lsId")String lsId)throws Exception{
        log.info("Starting deleteLastSchool method with lastSchoolId: {}", lsId);
        try {
            this.lastSchoolServiceImpl.deleteLastSchool(lsId);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Student deleted Successfully !");
            log.info("Successfully deleted last school with ID: {}", lsId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error deleting last school with ID: {}: {}", lsId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

}
